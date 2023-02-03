package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.PointPresentRequest;
import com.nhnacademy.booklay.server.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {
    Page<PointHistoryRetrieveResponse> retrievePointHistorys(Long memberNo, Pageable pageable);

    void createPointHistory(PointHistoryCreateRequest pointHistoryCreateRequest);

    TotalPointRetrieveResponse retrieveTotalPoint(Long memberNo);

    void presentPoint(Long memberNo, PointPresentRequest pointPresentRequest);
}
