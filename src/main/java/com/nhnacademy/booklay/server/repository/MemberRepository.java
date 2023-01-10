package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findAllBy(Pageable pageable);
    Optional<Member> findByMemberNo(Long memberNo);
    boolean existsByMemberId(String memberId);

}
