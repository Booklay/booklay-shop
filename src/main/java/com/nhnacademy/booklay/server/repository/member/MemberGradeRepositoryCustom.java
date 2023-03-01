package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberGradeRepositoryCustom {
    Page<MemberGradeRetrieveResponse> findByMemberMemberNo(Pageable pageable, Long memberNo);

    Optional<MemberGrade> retrieveCurrentMemberGrade(Long memberNo);
}
