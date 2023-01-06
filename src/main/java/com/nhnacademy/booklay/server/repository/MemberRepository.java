package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
