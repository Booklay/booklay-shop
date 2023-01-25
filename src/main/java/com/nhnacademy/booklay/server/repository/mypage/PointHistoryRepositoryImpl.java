package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.dto.member.reponse.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.entity.PointHistory;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QPointHistory;
import com.querydsl.core.types.Projections;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

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
            .where(pointHistory.member.memberNo.eq(memberNo))
            .orderBy(pointHistory.updatedAt.desc())
            .select(Projections.constructor(TotalPointRetrieveResponse.class,
                pointHistory.member.memberNo,
                pointHistory.totalPoint))
            .fetchOne();

        return Optional.ofNullable(responsePoint);
    }
}
