package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long> {
}
