package com.nhnacademy.booklay.server.exception.service;

public class AlreadyExistedException extends RuntimeException {
    public <T> AlreadyExistedException(Class<T> type, String message) {
        super("******** Failed to Create ********\n"
            + "Name     : " + type.getSimpleName() + "\n"
            + "Message  : " + message);
    }
}
