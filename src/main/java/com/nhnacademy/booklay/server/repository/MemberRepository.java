package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Page<Member> findAllBy(Pageable pageable);
    Member findByMemberId(Long memberId);

}
