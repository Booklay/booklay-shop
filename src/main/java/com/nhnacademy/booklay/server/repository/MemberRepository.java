package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.dto.member.MemberDto;
import com.nhnacademy.booklay.server.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<MemberDto> findAllBy();
    MemberDto findByMemberId(Long memberId);

}
