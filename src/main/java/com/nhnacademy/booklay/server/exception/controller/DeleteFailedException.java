package com.nhnacademy.booklay.server.exception.controller;

public class DeleteFailedException extends RuntimeException {
    public DeleteFailedException(String message) {
        super("******** Failed to Delete ********\n"
            + "Message  : " + message);
    }
}
