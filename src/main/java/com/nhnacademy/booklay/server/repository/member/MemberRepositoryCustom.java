package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MemberRepositoryCustom {

    Optional<MemberLoginResponse> retrieveMemberByUserId(String userId);

    Page<MemberRetrieveResponse> retrieveAll(Pageable pageable);

    Optional<MemberLoginResponse> retrieveMemberByEmail(String email);

    Page<DroppedMemberRetrieveResponse> retrieveDroppedMembers(Pageable pageable);

    Long retrieveValidMemberCount();

    Long retrieveBlockedMemberCount();

    Long retrieveDroppedMemberCount();

    Long retrieveWhiteGradeMemberCount();
    Long retrieveSilverGradeMemberCount();
    Long retrieveGoldGradeMemberCount();
    Long retrievePlatinumGradeMemberCount();

}
