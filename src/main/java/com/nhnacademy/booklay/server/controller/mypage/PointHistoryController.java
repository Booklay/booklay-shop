package com.nhnacademy.booklay.server.controller.mypage;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.member.reponse.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
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

    @GetMapping("/{memberNo}")
    public ResponseEntity<PageResponse<PointHistoryRetrieveResponse>> retrievePointHistory(
        Pageable pageable) {
        Page<PointHistoryRetrieveResponse> responsePage =
            pointHistoryService.retrievePointHistorys(pageable);

        PageResponse<PointHistoryRetrieveResponse> pointHistoryRetrieveResponsePageResponse
            = new PageResponse<>(responsePage.getNumber(), responsePage.getSize(),
            responsePage.getTotalPages(), responsePage.getContent());

        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(pointHistoryRetrieveResponsePageResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPointHistory(
        @Valid @RequestBody PointHistoryCreateRequest pointHistoryCreateRequest) {
        pointHistoryService.createPointHistory(pointHistoryCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
            .build();
    }
}
