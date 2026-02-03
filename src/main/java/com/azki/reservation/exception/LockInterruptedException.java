package com.azki.reservation.exception;

public class LockInterruptedException extends BaseException {
    public LockInterruptedException(String status, String message) {
        super(status, message);
    }
}
