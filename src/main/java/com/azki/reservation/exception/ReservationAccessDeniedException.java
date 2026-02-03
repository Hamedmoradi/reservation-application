package com.azki.reservation.exception;

public class ReservationAccessDeniedException extends BaseException {
    public ReservationAccessDeniedException(String status, String message) {
        super(status, message);
    }
}
