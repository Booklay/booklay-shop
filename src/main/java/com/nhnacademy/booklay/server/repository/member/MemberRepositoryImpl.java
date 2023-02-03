package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.QAuthority;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QMemberAuthority;
import com.nhnacademy.booklay.server.entity.QMemberGrade;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
/**
 * @author 양승아
 */
public class MemberRepositoryImpl extends QuerydslRepositorySupport
    implements MemberRepositoryCustom {

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
                                            authority.name,
                                            member.email))
            .fetchOne();

        return Optional.ofNullable(memberLoginResponse);
    }

    @Override
    public Page<MemberRetrieveResponse> retrieveAll(Pageable pageable) {
        QMember member = QMember.member;
        QMemberGrade memberGrade = QMemberGrade.memberGrade;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;
        QAuthority authority = QAuthority.authority;

        List<MemberRetrieveResponse> content = from(member)
            .innerJoin(memberGrade)
            .on(member.memberNo.eq(memberGrade.member.memberNo))
            .innerJoin(memberAuthority)
            .on(member.memberNo.eq(memberAuthority.member.memberNo))
            .innerJoin(authority)
            .on(authority.id.eq(memberAuthority.authority.id))
            .where(member.isBlocked.isFalse().and(member.deletedAt.isNull())
                .and(authority.name.notLike("ROLE_ADMIN")))
            .orderBy(member.createdAt.asc())
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

    @Override
    public Optional<MemberRetrieveResponse> retrieveMemberByEmail(String email) {
        QMember member = QMember.member;

        return Optional.ofNullable(from(member)
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
            .where(member.email.eq(email))
            .fetchOne());
    }

    public Page<DroppedMemberRetrieveResponse> retrieveDroppedMembers(Pageable pageable) {
        QMember member = QMember.member;

        List<DroppedMemberRetrieveResponse> content = from(member)
            .where(member.deletedAt.isNotNull())
            .select(Projections.constructor(DroppedMemberRetrieveResponse.class,
                member.memberId,
                member.deletedAt))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(member)
            .select(member.count());

        return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
    }

    @Override
    public Long retrieveValidMemberCount() {
        QMember member = QMember.member;

        return from(member)
            .where(member.isBlocked.isFalse().and(member.deletedAt.isNull()))
            .select(member.count())
            .fetchFirst();
    }

    @Override
    public Long retrieveBlockedMemberCount() {
        QMember member = QMember.member;

        return from(member)
            .where(member.isBlocked.isTrue())
            .select(member.count())
            .fetchFirst();
    }

    @Override
    public Long retrieveDroppedMemberCount() {
        QMember member = QMember.member;

        return from(member)
            .where(member.deletedAt.isNotNull())
            .select(member.count())
            .fetchFirst();
    }

    //TODO : return null 바꿔야 함
    @Override
    public Long retrieveWhiteGradeMemberCount() {
        QMember member = QMember.member;

        return null;
    }

    @Override
    public Long retrieveSilverGradeMemberCount() {
        return null;
    }

    @Override
    public Long retrieveGoldGradeMemberCount() {
        return null;
    }

    @Override
    public Long retrievePlatinumGradeMemberCount() {
        return null;
    }
}
