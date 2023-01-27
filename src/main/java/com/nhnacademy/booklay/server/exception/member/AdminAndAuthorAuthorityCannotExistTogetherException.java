package com.nhnacademy.booklay.server.exception.member;

public class AdminAndAuthorAuthorityCannotExistTogetherException extends RuntimeException {
    public AdminAndAuthorAuthorityCannotExistTogetherException() {
        super("Admin and author privileges cannot exist together");
    }
}
