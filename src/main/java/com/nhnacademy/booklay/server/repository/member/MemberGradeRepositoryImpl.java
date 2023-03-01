package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QMemberGrade;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;
import java.util.Optional;


public class MemberGradeRepositoryImpl extends QuerydslRepositorySupport
    implements MemberGradeRepositoryCustom {
    public MemberGradeRepositoryImpl() {
        super(MemberGrade.class);
    }

    @Override
    public Page<MemberGradeRetrieveResponse> findByMemberMemberNo(Pageable pageable,
                                                                  Long memberNo) {
        QMember member = QMember.member;
        QMemberGrade memberGrade = QMemberGrade.memberGrade;

        List<MemberGradeRetrieveResponse> content = from(memberGrade)
            .innerJoin(member)
            .on(member.memberNo.eq(memberGrade.member.memberNo))
            .where(member.memberNo.eq(memberNo))
            .orderBy(memberGrade.date.desc())
            .select(Projections.constructor(MemberGradeRetrieveResponse.class,
                memberGrade.id,
                member.memberNo,
                memberGrade.name,
                memberGrade.date))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(memberGrade)
            .innerJoin(member)
            .on(member.memberNo.eq(memberGrade.member.memberNo))
            .where(member.memberNo.eq(memberNo))
            .select(memberGrade.count());

        return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
    }

    @Override
    public Optional<MemberGrade> retrieveCurrentMemberGrade(Long memberNo) {
        QMemberGrade memberGrade = QMemberGrade.memberGrade;

        MemberGrade grade = from(memberGrade)
            .where(memberGrade.member.memberNo.eq(memberNo))
            .orderBy(memberGrade.date.desc())
            .select(Projections.constructor(MemberGrade.class,
                memberGrade.id,
                memberGrade.member,
                memberGrade.name,
                memberGrade.date))
            .fetchFirst();

        return Optional.ofNullable(grade);
    }


}
