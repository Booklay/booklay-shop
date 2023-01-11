package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MemberRepositoryImpl extends QuerydslRepositorySupport implements MemberRepositoryCustom {

    public MemberRepositoryImpl() {
        super(Member.class);
    }

    @Override
    public Page<MemberRetrieveResponse> retrieveAllProducts(Pageable pageable) {
        QMember member = QMember.member;

        QueryResults<MemberRetrieveResponse> result = from(member)
                .select(Projections.constructor(MemberRetrieveResponse.class,
                        member.memberId,
                        member.memberNo,
                        member.name,
                        member.email,
                        member.gender.name,
                        member.birthday,
                        member.createdAt,
                        member.isBlocked,
                        member.nickname,
                        member.password,
                        member.phoneNo,
                        member.updatedAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
