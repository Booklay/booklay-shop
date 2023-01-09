package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.dto.member.MemberDto;
import com.nhnacademy.booklay.server.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<MemberDto> findAllBy();
    MemberDto findByMemberId(Long memberId);

}
