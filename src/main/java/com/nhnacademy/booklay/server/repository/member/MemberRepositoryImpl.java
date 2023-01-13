package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.entity.Member;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public MemberLoginResponse retrieveMemberByUserId(String userId) {


        return null;
    }
}
