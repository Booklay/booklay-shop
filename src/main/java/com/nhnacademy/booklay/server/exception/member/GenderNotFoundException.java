package com.nhnacademy.booklay.server.exception.member;

public class GenderNotFoundException extends RuntimeException {
    public GenderNotFoundException(String genderName) {
        super("Gender Not Found, Gender Name : " + genderName);
    }
}
