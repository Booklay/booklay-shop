package com.nhnacademy.booklay.server.exception.member;

public class CreateMemberFailedException extends RuntimeException {
    public CreateMemberFailedException(String memberId) {
        super("Failed to Create Member \n"
            + "   ID : " + memberId);
    }
}
