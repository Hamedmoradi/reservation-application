package com.azki.reservation.model;

import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Accessors(chain = true)
public record ReservationResponse(Long reservationId, Long slotId, LocalDateTime startTime, LocalDateTime endTime) {
}