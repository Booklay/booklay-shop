package com.nhnacademy.booklay.server.controller.coupon;

import com.nhnacademy.booklay.server.dto.coupon.message.CouponIssueRequestMessage;
import com.nhnacademy.booklay.server.service.kafka.CouponZoneIssueService;
import com.nhnacademy.booklay.server.service.member.GetMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/member/coupon-zone")
@Slf4j
public class CouponZoneController {

    private final GetMemberService memberService;
    private final CouponZoneIssueService issueService;

    @PutMapping
    public ResponseEntity<Void> issueCoupon(@RequestBody CouponIssueRequestMessage request) {
        // TODO requestBody로 객체 받고, OK
        //  member 검증, mq에 저장, OK
        //  소켓 열기(randomValue),
        //  레디스에 저장(memberNo-couponNo : randomValue).
        memberService.getMemberNo(request.getMemberId());
        issueService.requestIssueCoupon(request);
        return null;
    }
}
