package com.nhnacademy.booklay.server.controller.coupon;

import com.nhnacademy.booklay.server.dto.coupon.request.CouponIssueRequest;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponIssueResponse;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponMemberResponse;
import com.nhnacademy.booklay.server.service.kafka.CouponZoneIssueService;
import com.nhnacademy.booklay.server.service.member.GetMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원의 쿠폰 발급 요청을 처리합니다.
 * @author 김승혜
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/member/coupon-zone")
@Slf4j
public class CouponZoneController {

    private final GetMemberService memberService;
    private final CouponZoneIssueService issueService;

    /**
     * 쿠폰 요청을 보낸 후에, 사용자가 응답을 요청하는 requestId를 보냅니다.
     */
    @PostMapping
    public ResponseEntity<CouponIssueResponse> issueCouponAtCouponZone(@RequestBody CouponIssueRequest request) {
        memberService.getMemberNo(request.getMemberId());

        String requestId = issueService.requestIssueCoupon(request);
        CouponIssueResponse response = new CouponIssueResponse(requestId);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }

    /**
     * 사용자가 requestId로 쿠푠 발급에 대한 응답을 요청합니다.
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<CouponMemberResponse> responseIssueCoupon(@PathVariable String requestId) {
        CouponMemberResponse response = issueService.getResponse(requestId);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(response);
    }
}
