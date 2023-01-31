package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MemberRepositoryCustom {

    Optional<MemberLoginResponse> retrieveMemberByUserId(String userId);

    Page<MemberRetrieveResponse> retrieveAll(Pageable pageable);

    Optional<MemberRetrieveResponse> retrieveMemberByEmail(String email);
    Page<DroppedMemberRetrieveResponse> retrieveDroppedMembers(Pageable pageable);

}
