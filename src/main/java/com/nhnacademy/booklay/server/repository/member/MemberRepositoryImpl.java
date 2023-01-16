package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.QAuthority;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QMemberAuthority;
import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Optional;

public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public Optional<MemberLoginResponse> retrieveMemberByUserId(String userId) {

        QMember member = QMember.member;
        QAuthority authority = QAuthority.authority;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;

        MemberLoginResponse memberLoginResponse = from(member)
                .innerJoin(memberAuthority).on(member.memberNo.eq(memberAuthority.pk.memberNo))
                .innerJoin(authority).on(memberAuthority.pk.authorityId.eq(authority.id))
                .where(member.memberId.eq(userId))
                .select(Projections.constructor(MemberLoginResponse.class,
                        member.memberId,
                        member.password,
                        authority.name))
                .fetchOne();

        return Optional.ofNullable(memberLoginResponse);
    }
}
