package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.entity.BlockedMemberDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedMemberDetailRepository extends JpaRepository<BlockedMemberDetail, Long> {
}
