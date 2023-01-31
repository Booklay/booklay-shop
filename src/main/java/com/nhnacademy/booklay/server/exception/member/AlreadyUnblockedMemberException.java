package com.nhnacademy.booklay.server.exception.member;

import com.nhnacademy.booklay.server.entity.Member;

public class AlreadyUnblockedMemberException extends RuntimeException {
    public AlreadyUnblockedMemberException(Member member) {
        super("Already unblocked member, Member No : " + member.getMemberNo() +
            ", Member Id: " + member.getMemberId());
    }
}
