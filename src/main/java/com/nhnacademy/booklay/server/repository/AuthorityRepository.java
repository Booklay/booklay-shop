package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
