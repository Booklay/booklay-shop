package com.nhnacademy.booklay.server.exception.member;

public class MemberAlreadyExistedException extends RuntimeException {
    public MemberAlreadyExistedException(String memberId) {
        super("Member Already Existed, Member ID : " + memberId);
    }
}
