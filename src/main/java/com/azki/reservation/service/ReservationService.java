package com.azki.reservation.service;

import com.azki.reservation.model.ReservationRequest;
import com.azki.reservation.model.ReservationResponse;

public interface ReservationService {
    ReservationResponse reserveNearestSlot(ReservationRequest request);

    void cancel(Long reserveId,Long userId);
}
