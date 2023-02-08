package com.nhnacademy.booklay.server.service.kafka;

import com.nhnacademy.booklay.server.dto.coupon.message.CouponIssueRequestMessage;
import com.nhnacademy.booklay.server.dto.coupon.message.CouponIssueResponseMessage;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponIssueRequest;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponMemberResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
    private final StringRedisTemplate redisTemplate;

    /**
     * 사용자의 발급 요청을 coupon 서버에 전달합니다.
     * 사용자의 요청에 requestId를 부여합니다.
     * 사용자는 requestId로 발급에 대한 응답을 요청합니다.
     * @param request 발급하려는 쿠폰의 id와 발급 대상 사용자 id
     */
    public String issueCoupon(CouponIssueRequest request) {
        String requestId = UUID.randomUUID().toString().substring(0, 30);
        CouponIssueRequestMessage message = new CouponIssueRequestMessage(request.getCouponId(),request.getMemberId(), requestId);

        kafkaTemplate.send(requestTopic, message);

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(requestId, "null");

        return requestId;
    }

    public CouponMemberResponse getResponse(String requestId) {
        ValueOperations<String, String> operations = redisTemplate.opsForValue();

        String message = operations.get(requestId);

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
