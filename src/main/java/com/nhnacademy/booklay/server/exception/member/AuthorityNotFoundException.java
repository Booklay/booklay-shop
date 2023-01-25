package com.nhnacademy.booklay.server.exception.member;

public class AuthorityNotFoundException extends RuntimeException {
    public AuthorityNotFoundException(String authorityName) {
        super("Authority Not Found, Authority Name : " + authorityName);
    }
}
