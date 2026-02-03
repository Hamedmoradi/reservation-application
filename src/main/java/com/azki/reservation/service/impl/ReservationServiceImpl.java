package com.azki.reservation.service.impl;

import com.azki.reservation.entity.AvailableSlot;
import com.azki.reservation.entity.Reservation;
import com.azki.reservation.entity.User;
import com.azki.reservation.enums.AppExceptionStatusEnum;
import com.azki.reservation.exception.*;
import com.azki.reservation.mapper.SlotMapper;
import com.azki.reservation.model.ReservationRequest;
import com.azki.reservation.model.ReservationResponse;
import com.azki.reservation.repository.AvailableSlotRepository;
import com.azki.reservation.repository.ReservationRepository;
import com.azki.reservation.repository.UserRepository;
import com.azki.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {
    private final AvailableSlotRepository slotRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RedissonClient redissonClient;
    private final SlotMapper slotMapper;

    private static final String LOCK_KEY = "lock:nearest_slot";

    @Transactional
    public ReservationResponse reserveNearestSlot(ReservationRequest request) {

        RLock lock = redissonClient.getLock(LOCK_KEY);

        try {
            // tryLock(waitTime, leaseTime)
            boolean locked = lock.tryLock(5, 10, TimeUnit.SECONDS);

            if (!locked) {
                throw new SystemBusyException(AppExceptionStatusEnum.SYSTEM_BUSY_EXCEPTION.getStatus(), AppExceptionStatusEnum.SYSTEM_BUSY_EXCEPTION.getMessage());
            }

            User user = userRepository.findById(request.userId()).orElseThrow(
                    () -> new UserNotFoundException(AppExceptionStatusEnum.USER_NOT_FOUND.getStatus(), AppExceptionStatusEnum.USER_NOT_FOUND.getMessage()));

            AvailableSlot slot = slotRepository
                    .findNearestAvailableSlot()
                    .orElseThrow(() -> new NoAvailableSlotException(AppExceptionStatusEnum.NO_AVAILABLE_SLOT.getStatus(), AppExceptionStatusEnum.NO_AVAILABLE_SLOT.getMessage()));

            if (slot.isReserved()) {
                throw new SlotAlreadyReservedException(AppExceptionStatusEnum.SLOT_ALREADY_RESERVED.getStatus(), AppExceptionStatusEnum.SLOT_ALREADY_RESERVED.getMessage());
            }

            slot.setReserved(true);
            slotRepository.save(slot);

            Reservation reservation = new Reservation();
            reservation.setUser(user);
            reservation.setAvailableSlot(slot);
            reservationRepository.save(reservation);

            return new ReservationResponse(reservation.getId(), slot.getId(), slot.getStartTime(), slot.getEndTime());

        } catch (InterruptedException e) {
            throw new LockInterruptedException(AppExceptionStatusEnum.LOCK_INTERRUPTED.getStatus(), AppExceptionStatusEnum.LOCK_INTERRUPTED.getMessage());
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    public void cancel(Long userId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new ReservationNotFoundException(AppExceptionStatusEnum.RESERVATION_NOT_FOUND.getStatus(),
                                AppExceptionStatusEnum.RESERVATION_NOT_FOUND.getMessage()));

        if (!reservation.getUser().getId().equals(userId)) {
            throw new ReservationAccessDeniedException(AppExceptionStatusEnum.RESERVATION_ACCESS_DENIED.getStatus(),
                    AppExceptionStatusEnum.RESERVATION_ACCESS_DENIED.getMessage());
        }

        AvailableSlot slot = slotRepository.findById(reservation.getAvailableSlot().getId())
                .orElseThrow(() ->
                        new AvailableSlotNotFoundException(AppExceptionStatusEnum.AVAILABLE_SLOT_NOT_FOUND.getStatus(),
                                AppExceptionStatusEnum.AVAILABLE_SLOT_NOT_FOUND.getMessage()));

        slot.setReserved(false);
        slotRepository.save(slot);
        reservationRepository.save(reservation);
    }
}
