package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<MemberRetrieveResponse> findAllBy(Pageable pageable);

    Optional<Member> findByMemberNo(Long memberNo);

    boolean existsByMemberId(String memberId);
}
