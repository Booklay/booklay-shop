package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.server.entity.MemberAuthority;
import com.nhnacademy.booklay.server.entity.QAuthority;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.entity.QMemberAuthority;
import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


public class MemberAuthorityRepositoryImpl extends QuerydslRepositorySupport
    implements MemberAuthorityRepositoryCustom {
    public MemberAuthorityRepositoryImpl() {
        super(MemberAuthority.class);
    }

    @Override
    public List<MemberAuthorityRetrieveResponse> retrieveAuthoritiesByMemberNo(Long memberNo) {
        QMember member = QMember.member;
        QMemberAuthority memberAuthority = QMemberAuthority.memberAuthority;
        QAuthority authority = QAuthority.authority;

        return from(member)
            .innerJoin(memberAuthority)
            .on(member.memberNo.eq(memberAuthority.member.memberNo))
            .innerJoin(authority)
            .on(authority.id.eq(memberAuthority.authority.id))
            .where(member.memberNo.eq(memberNo))
            .select(Projections.constructor(MemberAuthorityRetrieveResponse.class,
                authority.id,
                authority.name))
            .fetch();
    }
}
