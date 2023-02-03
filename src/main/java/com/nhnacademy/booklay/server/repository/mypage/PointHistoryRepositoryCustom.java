package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PointHistoryRepositoryCustom {

    Optional<TotalPointRetrieveResponse> retrieveLatestPointHistory(Long memberNo);

    Page<PointHistoryRetrieveResponse> retrievePointHistoryByMemberNo(Long memberNo,
                                                                      Pageable pageable);
}
