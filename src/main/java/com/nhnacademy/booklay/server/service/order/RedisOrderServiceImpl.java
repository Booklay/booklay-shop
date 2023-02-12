package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.OrderSheetSaveResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisOrderServiceImpl implements RedisOrderService {

    private final RedisTemplate<String, OrderSheet> redisTemplate;
    private static final String REDIS_KEY_PREFIX = "orderSheet";
    @Override
    public OrderSheetSaveResponse saveOrderSheet(OrderSheet orderSheet){
        String uuid = getUUID();
        String key = REDIS_KEY_PREFIX+uuid;
        redisTemplate.opsForSet().add(key, orderSheet);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
        return new OrderSheetSaveResponse(uuid);
    }

    @Override
    public OrderSheet retrieveOrderSheet(String orderId){
        return redisTemplate.opsForSet().pop(REDIS_KEY_PREFIX+orderId);
    }

    private String getUUID() {
        String uuid;
        while (true){
            uuid = UUID.randomUUID().toString();
            if (Boolean.TRUE.equals(redisTemplate.hasKey(REDIS_KEY_PREFIX+uuid))) {
                return uuid;
            }
        }
    }
}
