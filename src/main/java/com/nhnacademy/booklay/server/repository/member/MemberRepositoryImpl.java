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
    public Optional<Member> retrieveValidMemberByMemberNo(Long memberNo) {
        QMember qMember = QMember.member;

        Member member = from(qMember)
            .where(qMember.memberNo.eq(memberNo))
            .where(qMember.isBlocked.eq(false).and(qMember.deletedAt.isNull()))
            .select(Projections.constructor(Member.class,
                qMember.memberNo,
                qMember.gender,
                qMember.memberId,
                qMember.password,
                qMember.nickname,
                qMember.name,
                qMember.birthday,
                qMember.phoneNo,
                qMember.email,
                qMember.createdAt,
                qMember.updatedAt,
                qMember.deletedAt,
                qMember.isBlocked
            ))
            .fetchFirst();

        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> retrieveValidMemberByMemberId(String memberId) {
        QMember qMember = QMember.member;

        Member member = from(qMember)
            .where(qMember.memberId.eq(memberId))
            .where(qMember.isBlocked.eq(false).and(qMember.deletedAt.isNull()))
            .select(Projections.constructor(Member.class,
                qMember.memberNo,
                qMember.gender,
                qMember.memberId,
                qMember.password,
                qMember.nickname,
                qMember.name,
                qMember.birthday,
                qMember.phoneNo,
                qMember.email,
                qMember.createdAt,
                qMember.updatedAt,
                qMember.deletedAt,
                qMember.isBlocked
            ))
            .fetchFirst();

        return Optional.ofNullable(member);
    }

    @Override
    public Optional<MemberLoginResponse> retrieveMemberByUserId(String userId) {

        QMember member = QMember.member;
        QAuthority authority = QAuthority.authority;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;

        MemberLoginResponse memberLoginResponse = from(member)
            .innerJoin(memberAuthority).on(member.memberNo.eq(memberAuthority.pk.memberNo))
            .innerJoin(authority).on(memberAuthority.pk.authorityId.eq(authority.id))
            .where(member.memberId.eq(userId).and(member.deletedAt.isNull()))
            .select(Projections.constructor(MemberLoginResponse.class,
                member.memberId,
                member.password,
                authority.name,
                member.email,
                member.isBlocked))
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
            .distinct()
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(member)
            .select(member.count());
        return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
    }

    @Override
    public Optional<MemberLoginResponse> retrieveMemberByEmail(String email) {
        QMember member = QMember.member;
        QAuthority authority = QAuthority.authority;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;

        MemberLoginResponse memberLoginResponse = from(member)
            .innerJoin(memberAuthority).on(member.memberNo.eq(memberAuthority.pk.memberNo))
            .innerJoin(authority).on(memberAuthority.pk.authorityId.eq(authority.id))
            .where(member.email.eq(email))
            .select(Projections.constructor(MemberLoginResponse.class,
                                            member.memberId,
                                            member.password,
                                            authority.name,
                                            member.email))
            .fetchOne();

        return Optional.ofNullable(memberLoginResponse);
    }

    @Override
    public Optional<MemberRetrieveResponse> retrieveMemberInfoByEmail(String email) {

        QMember member = QMember.member;
        QAuthority authority = QAuthority.authority;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;
        QMemberGrade memberGrade = QMemberGrade.memberGrade;

        MemberRetrieveResponse memberRetrieveResponse = from(member)
            .innerJoin(memberAuthority).on(member.memberNo.eq(memberAuthority.pk.memberNo))
            .innerJoin(authority).on(memberAuthority.pk.authorityId.eq(authority.id))
            .innerJoin(memberGrade).on(member.memberNo.eq(memberGrade.member.memberNo))
            .where(member.email.eq(email))
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
                member.isBlocked,
                memberGrade.name,
                authority.name))
            .fetchFirst();

        return Optional.ofNullable(memberRetrieveResponse);
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
}
