package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.dto.member.reponse.TotalPointRetrieveResponse;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PointHistoryRepositoryCustom {

    Optional<TotalPointRetrieveResponse> retrieveLatestPointHistory(Long memberNo);
}
