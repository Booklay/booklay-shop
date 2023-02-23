package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.entity.MemberGrade;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberGradeRepositoryCustom {
    Page<MemberGradeRetrieveResponse> findByMember_MemberNo(Pageable pageable, Long memberNo);

    Optional<MemberGrade> retrieveCurrentMemberGrade(Long memberNo);


}
