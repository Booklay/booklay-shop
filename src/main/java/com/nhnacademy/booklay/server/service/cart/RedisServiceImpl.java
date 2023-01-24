package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RedisServiceImpl implements RedisCartService {
    RedisTemplate<String, CartDto> redisTemplate;
    @Override
    @Transactional(readOnly = true)
    public List<CartDto> getAllCartItems(String key) {
        return redisTemplate.<String, CartDto>opsForHash().values(key);
    }

    @Override
    public void setCartItem(String key, CartDto cartDto) {
        redisTemplate.opsForHash().put(key, cartDto.getProductNo(), cartDto);
        redisTemplate.expire(key, 2, TimeUnit.DAYS);
    }

    @Override
    public void deleteCartItem(String key, Long productNo) {
        redisTemplate.opsForHash().delete(key, productNo);
    }

    @Override
    public void deleteAllCartItems(String key) {
        redisTemplate.opsForHash().delete(key);
    }

    @Override
    public void deleteCartItems(String key, List<Long> productNoList) {
        redisTemplate.opsForHash().delete(key, productNoList);
    }


    @Override
    public Integer checkUUID(String uuid) {
        if (redisTemplate.hasKey(uuid)){
            redisTemplate.opsForHash().put(uuid, "used", 1);
            redisTemplate.expire(uuid, 2, TimeUnit.DAYS);
            return 1;
        }else {
            return 0;
        }
    }
}
