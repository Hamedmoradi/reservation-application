package com.azki.reservation.exception;

public class NoAvailableSlotException extends BaseException {
    public NoAvailableSlotException(String status, String message) {
        super(status, message);
    }
}
