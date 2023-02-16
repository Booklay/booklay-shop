package com.nhnacademy.booklay.server.controller.mypage;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.PointPresentRequest;
import com.nhnacademy.booklay.server.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 양승아
 */
@Slf4j
@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointHistoryController {
    private final PointHistoryService pointHistoryService;

    @PostMapping
    public ResponseEntity<Void> createPointHistory(
        @Valid @RequestBody PointHistoryCreateRequest pointHistoryCreateRequest) {
        pointHistoryService.createPointHistory(pointHistoryCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .build();
    }

    @GetMapping("/{memberNo}")
    public ResponseEntity<PageResponse<PointHistoryRetrieveResponse>> retrievePointHistory(
        @PathVariable Long memberNo, Pageable pageable) {
        Page<PointHistoryRetrieveResponse> responsePage =
            pointHistoryService.retrievePointHistorys(memberNo, pageable);

        PageResponse<PointHistoryRetrieveResponse> response = new PageResponse<>(responsePage);

        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(response);
    }

    @GetMapping("/total/{memberNo}")
    public ResponseEntity<TotalPointRetrieveResponse> retrieveTotalPoint(
        @PathVariable Long memberNo) {
        TotalPointRetrieveResponse totalPointRetrieveResponse =
            pointHistoryService.retrieveTotalPoint(memberNo);

        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(totalPointRetrieveResponse);
    }

    @PostMapping("/present/{memberNo}")
    public ResponseEntity<Void> presentPoint(@PathVariable Long memberNo,
                                             @Valid @RequestBody
                                             PointPresentRequest pointPresentRequest) {
        pointHistoryService.presentPoint(memberNo, pointPresentRequest);

        return ResponseEntity.status(HttpStatus.OK)
                             .build();
    }

}
