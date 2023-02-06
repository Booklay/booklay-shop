package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberGradeRepositoryCustom {
    Page<MemberGradeRetrieveResponse> findByMember_MemberNo(Pageable pageable, Long memberNo);
}
