package com.nhnacademy.booklay.server.exception.member;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long memberNo) {
        super("Member Not Found, MemberNo : " + memberNo);
    }

    public MemberNotFoundException(String memberId) {
        super("Member Not Found, Member Id : " + memberId);
    }
}
