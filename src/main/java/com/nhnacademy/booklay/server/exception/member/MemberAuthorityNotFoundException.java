package com.nhnacademy.booklay.server.exception.member;

public class MemberAuthorityNotFoundException extends RuntimeException {
    public MemberAuthorityNotFoundException() {
        super("MemberAuthority Not Found");
    }
}
