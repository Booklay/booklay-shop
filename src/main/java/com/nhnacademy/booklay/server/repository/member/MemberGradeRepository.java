package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGradeRepository extends JpaRepository<MemberGrade, Long> {
    Page<MemberGradeRetrieveResponse> findByMember_MemberNo(Pageable pageable, Long memberNo);
}
