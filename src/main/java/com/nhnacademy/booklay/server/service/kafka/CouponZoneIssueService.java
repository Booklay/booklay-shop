package com.nhnacademy.booklay.server.service.kafka;

import org.springframework.stereotype.Service;

@Service
public class CouponZoneIssueService {

    // kafkaTemplate
    public void requestIssueCoupon() {
        // TODO Topic 설정, 저장.
    }

    // Listener
    public void responseIssueCouponToMember() {
        // TODO mq확인(memberNo, couponNo, message) - 다른 토픽. 소켓으로 응답 보냄.
    }
}
