package com.azki.reservation.enums;


import lombok.Getter;

import java.io.IOException;
import java.util.Properties;

@Getter
public enum AppExceptionStatusEnum {

    AVAILABLE_SLOT_NOT_FOUND("AZKI-BackEnd-0001", "available.slot.not.found.exception"),
    RESERVATION_NOT_FOUND("AZKI-BackEnd-0002", "reservation.not.found.exception"),
    RESERVATION_ACCESS_DENIED("AZKI-BackEnd-0003", "reservation.access.denied.exception"),
    USER_NOT_FOUND("AZKI-BackEnd-0004", "user.not.found.exception"),
    INVALID_CREDENTIALS("AZKI-BackEnd-0005", "invalid.credentials.exception"),
    SYSTEM_BUSY_EXCEPTION("AZKI-BackEnd-0006", "system.busy.exception"),
    NO_AVAILABLE_SLOT("AZKI-BackEnd-0007", "no.available.slot"),
    SLOT_ALREADY_RESERVED("AZKI-BackEnd-0008", "slot.already.reserved"),
    LOCK_INTERRUPTED("AZKI-BackEnd-0009", "lock.interrupted"),

    ;


    private final String status;
    private final String message;

    AppExceptionStatusEnum(String status, String message) {
        this.status = status;
        this.message = MessageHolder.ERROR_MESSAGE_PROPERTIES.getProperty(message);
    }

    private static final class MessageHolder {
        private static final Properties ERROR_MESSAGE_PROPERTIES = new Properties();

        static {
            try {
                ERROR_MESSAGE_PROPERTIES.load(AppExceptionStatusEnum.class.getResourceAsStream("/messages.properties"));
            } catch (IOException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }
}
