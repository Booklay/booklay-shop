package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.member.reponse.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {
    Page<PointHistoryRetrieveResponse> retrievePointHistorys(Pageable pageable);

    void createPointHistory(PointHistoryCreateRequest pointHistoryCreateRequest);

    TotalPointRetrieveResponse retrieveTotalPoint(Long memberNo);
}
