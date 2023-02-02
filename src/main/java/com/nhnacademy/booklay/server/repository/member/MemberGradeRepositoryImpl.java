package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QMemberGrade;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;


public class MemberGradeRepositoryImpl extends QuerydslRepositorySupport
    implements MemberGradeRepositoryCustom {
    public MemberGradeRepositoryImpl() {
        super(MemberGrade.class);
    }

    @Override
    public Page<MemberGradeRetrieveResponse> findByMember_MemberNo(Pageable pageable,
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
}
