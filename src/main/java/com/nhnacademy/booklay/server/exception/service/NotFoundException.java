package com.nhnacademy.booklay.server.exception.service;

public class NotFoundException extends RuntimeException {
    public <T> NotFoundException(Class<T> type, String message) {
        super("******** Failed to Retrieve ********\n"
            + "Name     : " + type.getSimpleName() + "\n"
            + "Message  : " + message);
    }
}
