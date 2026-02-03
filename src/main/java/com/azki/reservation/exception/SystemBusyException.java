package com.azki.reservation.exception;

public class SystemBusyException extends BaseException {
    public SystemBusyException(String status, String message) {
        super(status, message);
    }
}
