package com.azki.reservation.exception;

public class ReservationNotFoundException extends BaseException {
    public ReservationNotFoundException(String status, String message) {
        super(status, message);
    }
}
