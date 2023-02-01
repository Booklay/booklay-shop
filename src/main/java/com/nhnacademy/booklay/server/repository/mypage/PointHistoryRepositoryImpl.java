package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.dto.member.reponse.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.entity.PointHistory;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QPointHistory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

public class PointHistoryRepositoryImpl extends QuerydslRepositorySupport
    implements PointHistoryRepositoryCustom {
    public PointHistoryRepositoryImpl() {
        super(PointHistory.class);
    }

    @Override
    public Optional<TotalPointRetrieveResponse> retrieveLatestPointHistory(Long memberNo) {
        QPointHistory pointHistory = QPointHistory.pointHistory;
        QMember member = QMember.member;

        TotalPointRetrieveResponse responsePoint = from(pointHistory)
            .innerJoin(member).on(pointHistory.member.memberNo.eq(member.memberNo))
            .where(pointHistory.member.memberNo.eq(memberNo).and(member.deletedAt.isNull()))
            .orderBy(pointHistory.updatedAt.asc())
            .select(Projections.constructor(TotalPointRetrieveResponse.class,
                                            pointHistory.member.memberNo,
                                            pointHistory.totalPoint))
            .fetchFirst();

        return Optional.ofNullable(responsePoint);
    }

    @Override
    public Page<PointHistoryRetrieveResponse> retrievePointHistoryByMemberNo(Long memberNo,
                                                                             Pageable pageable) {
        QPointHistory pointHistory = QPointHistory.pointHistory;
        QMember member = QMember.member;

        List<PointHistoryRetrieveResponse> content = from(pointHistory)
            .innerJoin(member).on(pointHistory.member.memberNo.eq(member.memberNo))
            .where(pointHistory.member.memberNo.eq(memberNo).and(member.deletedAt.isNull()))
            .orderBy(pointHistory.updatedAt.desc())
            .select(Projections.constructor(PointHistoryRetrieveResponse.class,
                                            pointHistory.id,
                                            pointHistory.member.memberNo,
                                            pointHistory.point,
                                            pointHistory.totalPoint,
                                            pointHistory.updatedAt,
                                            pointHistory.updatedDetail))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(pointHistory)
            .innerJoin(member).on(pointHistory.member.memberNo.eq(member.memberNo))
            .where(pointHistory.member.memberNo.eq(memberNo))
            .select(pointHistory.count());

        return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
    }
}
