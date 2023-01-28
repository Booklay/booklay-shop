package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom{
    Optional<Member> findByMemberNo(Long memberNo);

    Optional<Member> findByMemberId(String memberId);

    boolean existsByMemberId(String memberId);
}
