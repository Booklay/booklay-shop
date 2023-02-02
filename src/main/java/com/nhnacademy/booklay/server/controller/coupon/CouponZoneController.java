package com.nhnacademy.booklay.server.controller.coupon;

import com.nhnacademy.booklay.server.service.kafka.CouponZoneIssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/coupon-zone/{memberNo}")
public class CouponZoneController {

    private final CouponZoneIssueService issueService;

    @GetMapping
    public ResponseEntity<Void> issueCoupon(@PathVariable Long memberNo) {
        // TODO requestBody로 객체 받고, member 검증, mq에 저장, 소켓 열기(randomValue), 레디스에 저장(memberNo-couponNo : randomValue).
        issueService.requestIssueCoupon();
        return null;
    }
}
