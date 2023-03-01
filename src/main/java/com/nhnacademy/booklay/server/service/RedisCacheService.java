package com.nhnacademy.booklay.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisCacheService {
    @Qualifier("StringTypeRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;

    @Qualifier("serverIp")
    private final String serverIp;
    private static final String KEY_NAME_SERVER_COUNT = "ServerCountCheck";

    /**
     * 레디스에서 캐시의업데이트 사항이 있는지 체크함
     * @param keyName 키 preFix 캐시의 구획이라고 생각하면 좋음
     * @return 부른 캐시클래스에서 삭제해야할 리스트
     */
    public List<Long> updateCheck(String keyName){
        HashOperations<String, String, String> updateHash = redisTemplate.opsForHash();
        updateHash.put(KEY_NAME_SERVER_COUNT, serverIp, LocalDateTime.now().toString());
        //현재 해당 기록을 사용하는 모든 서버의 수
        long serverCount = updateHash.size(KEY_NAME_SERVER_COUNT);
        Set<String> changedProductSet = updateHash.keys(keyName);
        List<String> redisDeleteList = new ArrayList<>();
        List<Long> mapDeleteList = new ArrayList<>();
        for (String productId : changedProductSet) {
            Set<String> productUpdatedConfirmSet =
                updateHash.keys(keyName + productId);
            //이미 캐시 삭제를 적용한건지 확인
            if (!productUpdatedConfirmSet.contains(serverIp)){
                mapDeleteList.add(Long.parseLong(productId));
            }
            if (productUpdatedConfirmSet.size() >= serverCount - 1) {
                redisDeleteList.add(productId);
            } else {
                updateHash.put(keyName + productId, serverIp, "");
            }
        }
        //해당 리스트는 생존서버의 수와 캐리삭제처리를 한 서버의 수가 동일한경우 추가되어
        //레디스에서 캐리처리 목록에서 삭제됨
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
        updateHash.delete(deleteKey, hashSet.toArray());
    }

    /**
     * 캐시를 삭제하도록 레디스에 등록함
     * @param keyName key preFix
     * @param keyNo key
     */
    public void deleteCache(String keyName, Long keyNo){
        if (keyNo!= null){
            HashOperations<String, String, String> updateHash = redisTemplate.opsForHash();
            deleteCheckedCache(keyName+keyNo);
            updateHash.put(keyName, keyNo.toString(), "");
        }
    }


    /**
     * 서버가 updateCheck 시 자신의 ip를 키로 생존신고를 함
     * 10분이상 시간 업데이트가 되지 않을 경우 해당 키를 삭제함
     */
    @Scheduled(cron = "0 0/1 * * * *")
    private void checkServerAlive(){
        HashOperations<String, String, String> updateHash = redisTemplate.opsForHash();
        Set<String> keySet = updateHash.keys(KEY_NAME_SERVER_COUNT);
        for(String serversIp : keySet){
            String lastUpdatedTime = updateHash.get(KEY_NAME_SERVER_COUNT, serversIp);
            if (lastUpdatedTime!= null && LocalDateTime.now().minusMinutes(1).toString().compareTo(lastUpdatedTime) > 0){
                updateHash.delete(KEY_NAME_SERVER_COUNT, serverIp);
            }
        }
    }
}
