package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.entity.BlockedMemberDetail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedMemberDetailRepository
    extends JpaRepository<BlockedMemberDetail, Long>, BlockedMemberDetailRepositoryCustom {
    Optional<BlockedMemberDetail> findFirstByMember_MemberNoOrderByBlockedAtDesc(Long memberNo);
    Optional<BlockedMemberDetail> findById(Long id);
}
