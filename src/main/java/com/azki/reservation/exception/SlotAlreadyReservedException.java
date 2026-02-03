package com.azki.reservation.exception;

public class SlotAlreadyReservedException extends BaseException {
    public SlotAlreadyReservedException(String status, String message) {
        super(status, message);
    }
}
