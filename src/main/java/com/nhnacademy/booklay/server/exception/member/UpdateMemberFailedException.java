package com.nhnacademy.booklay.server.exception.member;

public class UpdateMemberFailedException extends RuntimeException {
    public UpdateMemberFailedException(Long memberNo) {
        super("Failed to Update Category \n"
                  + "   ID : " + memberNo);
    }
}
