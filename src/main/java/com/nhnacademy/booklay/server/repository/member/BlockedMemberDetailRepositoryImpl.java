package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.entity.BlockedMemberDetail;
import com.nhnacademy.booklay.server.entity.QBlockedMemberDetail;
import com.nhnacademy.booklay.server.entity.QMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

public class BlockedMemberDetailRepositoryImpl extends QuerydslRepositorySupport
    implements BlockedMemberDetailRepositoryCustom {
    public BlockedMemberDetailRepositoryImpl() {
        super(BlockedMemberDetail.class);
    }

    @Override
    public Page<BlockedMemberRetrieveResponse> retrieveBlockedMembers(Pageable pageable) {
        QMember member = QMember.member;
        QBlockedMemberDetail blockedMemberDetail = QBlockedMemberDetail.blockedMemberDetail;

        List<BlockedMemberRetrieveResponse> content = from(blockedMemberDetail)
            .innerJoin(member)
            .on(member.memberNo.eq(blockedMemberDetail.member.memberNo))
            .where(member.isBlocked.eq(true))
            .orderBy(blockedMemberDetail.blockedAt.desc())
            .select(Projections.constructor(BlockedMemberRetrieveResponse.class,
                                            blockedMemberDetail.id,
                                            member.memberNo,
                                            member.memberId,
                                            member.name,
                                            blockedMemberDetail.reason,
                                            blockedMemberDetail.blockedAt,
                                            blockedMemberDetail.releasedAt))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(blockedMemberDetail)
            .innerJoin(member)
            .on(member.memberNo.eq(blockedMemberDetail.member.memberNo))
            .select(blockedMemberDetail.count());

        return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
    }

    @Override
    public Page<BlockedMemberRetrieveResponse> retrieveBlockedMemberDetail(Long memberNo,
                                                                           Pageable pageable) {
        QMember member = QMember.member;
        QBlockedMemberDetail blockedMemberDetail = QBlockedMemberDetail.blockedMemberDetail;

        List<BlockedMemberRetrieveResponse> content = from(blockedMemberDetail)
            .innerJoin(member)
            .on(member.memberNo.eq(blockedMemberDetail.member.memberNo))
            .where(member.memberNo.eq(memberNo))
            .orderBy(blockedMemberDetail.blockedAt.desc())
            .select(Projections.constructor(BlockedMemberRetrieveResponse.class,
                blockedMemberDetail.id,
                member.memberNo,
                member.memberId,
                member.name,
                blockedMemberDetail.reason,
                blockedMemberDetail.blockedAt,
                blockedMemberDetail.releasedAt))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(blockedMemberDetail)
            .innerJoin(member)
            .on(member.memberNo.eq(blockedMemberDetail.member.memberNo))
            .select(blockedMemberDetail.count());

        return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
    }
}
