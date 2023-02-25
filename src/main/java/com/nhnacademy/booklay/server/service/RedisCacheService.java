package com.nhnacademy.booklay.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisCacheService {
    @Qualifier("StringTypeRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    @Qualifier("serverIp")
    private final String serverIp;
    private static final String KEY_NAME_SERVER_COUNT = "ServerCountCheck";

    public List<Long> updateCheck(String keyName, Map<Long, ?> map){
        HashOperations<String, String, String> updateHash = redisTemplate.opsForHash();
        updateHash.put(KEY_NAME_SERVER_COUNT, serverIp, LocalDateTime.now().toString());
        long serverCount = updateHash.size(KEY_NAME_SERVER_COUNT);
        Set<String> changedProductSet = updateHash.keys(keyName);
        List<String> hasList = changedProductSet.stream().filter(s -> map.containsKey(Long.parseLong(s))).collect(Collectors.toList());
        List<String> redisDeleteList = new ArrayList<>();
        List<Long> mapDeleteList = new ArrayList<>();
        for (String productId : hasList) {
            Set<String> productUpdatedConfirmSet =
                updateHash.keys(keyName + productId);
            if (!productUpdatedConfirmSet.contains(serverIp)){
                mapDeleteList.add(Long.parseLong(productId));
            }
            if (productUpdatedConfirmSet.size() >= serverCount - 1) {
                redisDeleteList.add(productId);
            } else {
                updateHash.put(keyName + productId, serverIp, "");
            }
        }
        if (!redisDeleteList.isEmpty()){
            updateHash.delete(keyName, redisDeleteList.toArray());
            deleteCheckedCache(keyName, redisDeleteList);
        }
        return mapDeleteList;
    }

    private void deleteCheckedCache(String keyPrefix, List<String> deleteList){
        for (String productNo:deleteList){
            deleteCheckedCache(keyPrefix+productNo);
        }
    }
    private void deleteCheckedCache(String deleteKey){
        HashOperations<String, String, String> updateHash = redisTemplate.opsForHash();
        Set<String> hashSet = updateHash.keys(deleteKey);
        if (hashSet.isEmpty()){
            return;
        }
        updateHash.delete(deleteKey, hashSet);
    }
    public void deleteCache(String keyName, Long keyNo){
        HashOperations<String, String, String> updateHash = redisTemplate.opsForHash();
        deleteCheckedCache(keyName+keyNo);
        updateHash.put(keyName, keyNo.toString(), "");
    }
}
