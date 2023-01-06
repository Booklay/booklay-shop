package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender, Long> {
}
