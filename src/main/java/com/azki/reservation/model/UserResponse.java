package com.azki.reservation.model;

import java.time.LocalDateTime;

public record UserResponse(Long id, String username, String email, String password, LocalDateTime createdAt) {
}
