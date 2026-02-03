package com.azki.reservation.exception;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException(String status, String message) {
        super(status, message);
    }
}
