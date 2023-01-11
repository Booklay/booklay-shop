package com.nhnacademy.booklay.server.exception.member;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long memberNo) {
        super("Member Not Found, MemberNo : " + memberNo);
    }
}
