package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MemberRepositoryCustom {
    Optional<Member> retrieveValidMemberByMemberNo(Long memberNo);

    Optional<Member> retrieveValidMemberByMemberId(String memberId);

    Optional<MemberLoginResponse> retrieveMemberByUserId(String userId);

    Page<MemberRetrieveResponse> retrieveAll(Pageable pageable);

    Optional<MemberLoginResponse> retrieveMemberByEmail(String email);

    Optional<MemberRetrieveResponse> retrieveMemberInfoByEmail(String email);

    Page<DroppedMemberRetrieveResponse> retrieveDroppedMembers(Pageable pageable);

    Long retrieveValidMemberCount();

    Long retrieveBlockedMemberCount();

    Long retrieveDroppedMemberCount();

}
