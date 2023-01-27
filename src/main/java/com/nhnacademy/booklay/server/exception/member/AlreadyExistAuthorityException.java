package com.nhnacademy.booklay.server.exception.member;

public class AlreadyExistAuthorityException extends RuntimeException {
    public AlreadyExistAuthorityException(String authorityName) {
        super("Already Exist Authority, Authority Name : " + authorityName);
    }
}
