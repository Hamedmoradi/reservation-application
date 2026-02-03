package com.azki.reservation.model;

import java.time.LocalDateTime;

public record SlotRequest(Long id, LocalDateTime startTime, LocalDateTime endTime) {
}
