package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.response.BlockedMemberRetrieveResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlockedMemberDetailRepositoryCustom {
    Page<BlockedMemberRetrieveResponse> retrieveBlockedMembers(Pageable pageable);

    Page<BlockedMemberRetrieveResponse> retrieveBlockedMemberDetail(Long memberNo, Pageable pageable);
}
