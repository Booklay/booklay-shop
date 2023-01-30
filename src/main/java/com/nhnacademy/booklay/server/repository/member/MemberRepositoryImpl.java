package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.QAuthority;
import com.nhnacademy.booklay.server.entity.QBlockedMemberDetail;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QMemberAuthority;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

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

    @Override
    public Page<MemberRetrieveResponse> retrieveAll(Pageable pageable) {
        QMember member = QMember.member;

        List<MemberRetrieveResponse> content = from(member)
            .select(Projections.constructor(MemberRetrieveResponse.class,
                member.memberNo,
                member.gender.name,
                member.memberId,
                member.nickname,
                member.name,
                member.birthday,
                member.phoneNo,
                member.email,
                member.createdAt,
                member.updatedAt,
                member.deletedAt,
                member.isBlocked))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(member)
            .select(member.count());
        return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
    }

}
