package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
