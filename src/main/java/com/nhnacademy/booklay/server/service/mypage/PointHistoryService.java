package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.member.reponse.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.PointPresentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PointHistoryService {
    Page<PointHistoryRetrieveResponse> retrievePointHistorys(Long memberNo, Pageable pageable);

    void createPointHistory(PointHistoryCreateRequest pointHistoryCreateRequest);

    TotalPointRetrieveResponse retrieveTotalPoint(Long memberNo);

    void presentPoint(Long memberNo, PointPresentRequest pointPresentRequest);
}
