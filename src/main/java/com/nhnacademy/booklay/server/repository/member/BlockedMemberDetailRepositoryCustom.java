package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.entity.BlockedMemberDetail;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlockedMemberDetailRepositoryCustom {
    Page<BlockedMemberRetrieveResponse> retrieveBlockedMembers(Pageable pageable);

    Page<BlockedMemberRetrieveResponse> retrieveBlockedMemberDetail(Long memberNo, Pageable pageable);
}
