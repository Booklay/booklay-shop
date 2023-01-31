package com.nhnacademy.booklay.server.exception.member;

import com.nhnacademy.booklay.server.entity.Member;

public class AlreadyBlockedMemberException extends RuntimeException {
    public AlreadyBlockedMemberException(Member member) {
        super("Already blocked member, Member No : " + member.getMemberNo() +
            ", Member Id: " + member.getMemberId());
    }
}
