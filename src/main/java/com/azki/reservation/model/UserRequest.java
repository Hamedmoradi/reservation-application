package com.azki.reservation.model;

import com.azki.reservation.validation.anotation.Email;

public record UserRequest(String username, @Email String email, String password) {
}
