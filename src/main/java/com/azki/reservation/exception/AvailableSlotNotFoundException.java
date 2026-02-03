package com.azki.reservation.exception;

public class AvailableSlotNotFoundException extends BaseException {
    public AvailableSlotNotFoundException(String status, String message) {
        super(status, message);
    }
}
