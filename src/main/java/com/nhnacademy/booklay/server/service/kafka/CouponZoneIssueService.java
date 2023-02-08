package com.nhnacademy.booklay.server.service.kafka;

import com.nhnacademy.booklay.server.dto.coupon.message.CouponIssueRequestMessage;
import com.nhnacademy.booklay.server.dto.coupon.message.CouponIssueResponseMessage;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponIssueRequest;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponMemberResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponZoneIssueService {

    @Value(value = "${message.topic.coupon.request}")
    private String requestTopic;

    private final StringRedisTemplate redisTemplate;
    private final KafkaTemplate<String, CouponIssueRequestMessage> kafkaTemplate;

    public String requestIssueCoupon(CouponIssueRequest request) {
        log.info(request.getCouponId() + " :: 쿠폰으로 보낼겁니다.");

        String uuid = UUID.randomUUID().toString().substring(0, 30);
        CouponIssueRequestMessage message = new CouponIssueRequestMessage(request.getCouponId(),request.getMemberId(), uuid);
        kafkaTemplate.send(requestTopic, message);

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(uuid, "null");

        return uuid;
    }

    public CouponMemberResponse getResponse(String requestId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        String message = operations.get(requestId);
        log.info("도착한 메시지!!!! :: " + message);

        CouponMemberResponse response = new CouponMemberResponse(message);
        return response;
    }

    @KafkaListener(topics = "${message.topic.coupon.response}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void responseIssueCouponToMember(CouponIssueResponseMessage message) {
        log.info(message.getMessage() + " :: 쿠폰으로부터 받은 응답 메시지.");
        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(message.getUuid(), message.getMessage());
        // TODO mq확인(memberNo, couponNo, message) - 다른 토픽. 소켓으로 응답 보냄.
    }
}
