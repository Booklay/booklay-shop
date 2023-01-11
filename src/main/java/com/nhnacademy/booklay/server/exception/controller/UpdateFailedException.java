package com.nhnacademy.booklay.server.exception.controller;

public class UpdateFailedException extends RuntimeException {
    public UpdateFailedException(String message) {
        super("******** Failed to Update ********\n"
            + "Message  : " + message);
    }
}
