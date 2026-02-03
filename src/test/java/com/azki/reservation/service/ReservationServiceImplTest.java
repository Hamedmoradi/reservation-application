package com.azki.reservation.service;

import com.azki.reservation.entity.AvailableSlot;
import com.azki.reservation.entity.BaseEntity;
import com.azki.reservation.entity.Reservation;
import com.azki.reservation.entity.User;
import com.azki.reservation.exception.ReservationAccessDeniedException;
import com.azki.reservation.exception.ReservationNotFoundException;
import com.azki.reservation.mapper.SlotMapper;
import com.azki.reservation.model.ReservationRequest;
import com.azki.reservation.model.ReservationResponse;
import com.azki.reservation.repository.AvailableSlotRepository;
import com.azki.reservation.repository.ReservationRepository;
import com.azki.reservation.repository.UserRepository;
import com.azki.reservation.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private AvailableSlotRepository slotRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @Mock
    private SlotMapper slotMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private ReservationRequest request;
    private User user;
    private AvailableSlot slot;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        request = new ReservationRequest(1L);

        user = new User();
        setId(user, 1L);

        slot = new AvailableSlot();
        setId(user, 10L);
        slot.setStartTime(LocalDateTime.now());
        slot.setEndTime(LocalDateTime.now().plusHours(1));
        slot.setReserved(false);

        reservation = new Reservation();
        setId(user, 100L);
        reservation.setUser(user);
        reservation.setAvailableSlot(slot);
    }


    // ---------- reserveNearestSlot ----------
    @Test
    void reserveNearestSlot_success() throws Exception {
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(5, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(rLock.isHeldByCurrentThread()).thenReturn(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findNearestAvailableSlot()).thenReturn(Optional.of(slot));

        when(slotRepository.save(any())).thenReturn(slot);
        when(reservationRepository.save(any())).thenReturn(reservation);

        ReservationResponse response = reservationService.reserveNearestSlot(request);

        assertNotNull(response);
        assertEquals(slot.getId(), response.slotId());

        verify(rLock).unlock();
        verify(slotRepository).save(slot);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void reserveNearestSlot_lockNotAcquired_shouldThrowException() throws Exception {
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(5, 10, TimeUnit.SECONDS)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> reservationService.reserveNearestSlot(request));
    }

    @Test
    void reserveNearestSlot_userNotFound() throws Exception {
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(5, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reservationService.reserveNearestSlot(request));
    }

    @Test
    void reserveNearestSlot_noAvailableSlot() throws Exception {
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(5, 10, TimeUnit.SECONDS)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(slotRepository.findNearestAvailableSlot()).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> reservationService.reserveNearestSlot(request));
    }

    // ---------- cancel ----------
//    @Test
//    void cancel_success() {
//        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));
//        when(slotRepository.findById(10L)).thenReturn(Optional.of(slot));
//
//        reservationService.cancel(1L, 100L);
//
//        assertFalse(slot.isReserved());
//        verify(slotRepository).save(slot);
//        verify(reservationRepository).save(reservation);
//    }


    @Test
    void cancel_reservationNotFound() {
        when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class,
                () -> reservationService.cancel(1L, 100L));
    }

    @Test
    void cancel_accessDenied() {
        User otherUser = new User();
        setId(otherUser, 2L);
        reservation.setUser(otherUser);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));

        assertThrows(ReservationAccessDeniedException.class,
                () -> reservationService.cancel(1L, 100L));
    }


//    @Test
//    void cancel_slotNotFound() {
//        reservation.setAvailableSlot(slot);
//
//        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
//
//        when(slotRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        assertThrows(ReservationAccessDeniedException.class,
//                () -> reservationService.cancel(3L, 100L));
//    }

    private void setId(BaseEntity entity, Long id) {
        try {
            var field = BaseEntity.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
