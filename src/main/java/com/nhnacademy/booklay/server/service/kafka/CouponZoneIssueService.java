package com.nhnacademy.booklay.server.service.kafka;

import com.nhnacademy.booklay.server.dto.coupon.message.CouponIssueRequestMessage;
import com.nhnacademy.booklay.server.dto.coupon.message.CouponIssueResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponZoneIssueService {

    @Value(value = "${message.topic.coupon.request}")
    private String requestTopic;

    private final KafkaTemplate<String, CouponIssueRequestMessage> kafkaTemplate;

    public void requestIssueCoupon(CouponIssueRequestMessage request) {
        log.info(request.getCouponId() + " : requestIssueCoupon");
        kafkaTemplate.send(requestTopic, request);
    }

    @KafkaListener(topics = "${message.topic.coupon.response}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void responseIssueCouponToMember(CouponIssueResponseMessage message) {
        log.info(message.getMessage());
        // TODO mq확인(memberNo, couponNo, message) - 다른 토픽. 소켓으로 응답 보냄.
    }
}
