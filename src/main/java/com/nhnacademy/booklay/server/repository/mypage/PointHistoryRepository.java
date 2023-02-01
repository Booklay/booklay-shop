package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.dto.member.reponse.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.entity.PointHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository
    extends JpaRepository<PointHistory, Long>, PointHistoryRepositoryCustom {
    Page<PointHistoryRetrieveResponse> findAllBy(Pageable pageable);

    void deleteAllByMember_MemberNo(Long memberNo);
}
