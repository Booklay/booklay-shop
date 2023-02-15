package com.nhnacademy.booklay.server.exception.controller;

public class CreateFailedException extends RuntimeException {
    public CreateFailedException(String message) {
        super("\n******** Failed to Create ********\n"
                  + "Message  : " + message);
    }
}
