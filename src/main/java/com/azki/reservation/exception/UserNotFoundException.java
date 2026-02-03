package com.azki.reservation.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String status, String message) {
        super(status, message);
    }
}
