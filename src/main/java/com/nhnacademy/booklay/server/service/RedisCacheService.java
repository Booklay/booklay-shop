package com.nhnacademy.booklay.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisCacheService {
    @Qualifier("StringTypeRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    @Qualifier("serverIp")
    private final String serverIp;
    private static final String KEY_NAME_SERVER_COUNT = "ServerCountCheck";

    public List<Long> updateCheck(String keyName, Map<Long, ?> map){
        HashOperations<String, Long, String> updateHash = redisTemplate.opsForHash();
        redisTemplate.<String, String>opsForHash().put(KEY_NAME_SERVER_COUNT, serverIp, LocalDateTime.now().toString());
        long serverCount = redisTemplate.<String, String>opsForHash().size(KEY_NAME_SERVER_COUNT);
        Set<Long> changedProductSet = updateHash.keys(keyName);
        List<Long> hasList = changedProductSet.stream().filter(map::containsKey).collect(Collectors.toList());
        List<Long> redisDeleteList = new ArrayList<>();
        List<Long> mapDeleteList = new ArrayList<>();
        for (Long productId : hasList) {
            Set<String> productUpdatedConfirmSet =
                    redisTemplate.<String, String>opsForHash().keys(keyName + productId);
            if (!productUpdatedConfirmSet.contains(serverIp)){
                mapDeleteList.add(productId);
            }
            if (productUpdatedConfirmSet.size() >= serverCount - 1) {
                redisDeleteList.add(productId);
            } else {
                redisTemplate.<String, String>opsForHash().put(keyName + productId, serverIp, "");
            }
        }
        if (!redisDeleteList.isEmpty()){
            updateHash.delete(keyName, redisDeleteList);
        }
        return mapDeleteList;
    }

    public void deleteCache(String keyName, Long keyNo){
        HashOperations<String, Long, String> updateHash = redisTemplate.opsForHash();
        updateHash.put(keyName, keyNo, "");
    }
}
