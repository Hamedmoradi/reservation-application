package com.azki.reservation.exception;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String status, String message) {
        super(status, message);
    }
}
