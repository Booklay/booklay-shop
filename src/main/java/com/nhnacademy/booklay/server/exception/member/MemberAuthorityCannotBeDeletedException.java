package com.nhnacademy.booklay.server.exception.member;

public class MemberAuthorityCannotBeDeletedException extends RuntimeException {
    public MemberAuthorityCannotBeDeletedException() {
        super("Member Authority cannot be deleted");
    }
}
