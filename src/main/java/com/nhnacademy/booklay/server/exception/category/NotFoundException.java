package com.nhnacademy.booklay.server.exception.category;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName, Long inputId) {
        super("**********Element Not Found**********"
                  + "\n Entity Name : " + entityName
                  + "\n Input ID : " + inputId);

    }
}
