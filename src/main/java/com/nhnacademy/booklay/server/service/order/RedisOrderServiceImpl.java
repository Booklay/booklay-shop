package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisOrderServiceImpl implements RedisOrderService {

    private final RedisTemplate<String, OrderSheet> redisTemplate;
    private static final String REDIS_KEY_PREFIX = "orderSheet";
    @Override
    public String saveOrderSheet(OrderSheet orderSheet){
        String uuid = getUUID();
        String key = REDIS_KEY_PREFIX+uuid;
        redisTemplate.opsForValue().set(key, orderSheet);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
        return uuid;
    }

    @Override
    public OrderSheet retrieveOrderSheet(String orderId){
        return redisTemplate.opsForValue().get(REDIS_KEY_PREFIX+orderId);
    }

    private String getUUID() {
        String uuid;
        while (true){
            uuid = UUID.randomUUID().toString();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX+uuid))) {
                return uuid;
            }
        }
    }
}
