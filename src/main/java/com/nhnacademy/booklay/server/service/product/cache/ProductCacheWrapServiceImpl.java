package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.cache.ProductAllInOneWrapDto;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCacheWrapServiceImpl implements ProductCacheWrapService {
    @Qualifier("StringTypeRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;
    private final ProductService productService;
    private ProductAllInOneWrapDto first;
    private ProductAllInOneWrapDto last;
    private final Map<Long, ProductAllInOneWrapDto> allInOneWrapDtoMap = new HashMap<>();
    //맵에 넣기 위한 대기열
    private final List<ProductAllInOneResponse> tempList = Collections.synchronizedList(new LinkedList<>());
    @Qualifier("serverIp")
    private final String serverIp;
    private static final int MAX_CACHE_NUM = 200;

    @Override
    public ProductAllInOneResponse cacheRetrieveProductAllInOne(Long productId){
        ProductAllInOneWrapDto wrapDto = allInOneWrapDtoMap.get(productId);
        ProductAllInOneResponse response = wrapDto==null?productService.findProductById(productId):wrapDto.getData();
        tempList.add(response);
        return response;
    }


    private static final String KEY_NAME = "productCache";
    private static final String KEY_NAME_SERVER_COUNT = "productCacheServerCheck";
    @Override
    @Scheduled(cron = "0/1 * * * * *")
    public void updateCheck(){
        HashOperations<String, Long, List<String>> updateHash = redisTemplate.opsForHash();
        redisTemplate.<String, String>opsForHash().put(KEY_NAME_SERVER_COUNT, serverIp, LocalDateTime.now().toString());
        long serverCount = updateHash.size(KEY_NAME_SERVER_COUNT);
        Set<Long> changedProductSet = updateHash.keys(KEY_NAME);
        changedProductSet.stream().filter(allInOneWrapDtoMap::containsKey).forEach(this::deleteFromMap);
        List<Long> deleteList = new ArrayList<>();
        for (Long productId : changedProductSet) {
            Set<String> productUpdatedConfirmSet =
                redisTemplate.<String, String>opsForHash().keys(KEY_NAME + productId);
            if (!productUpdatedConfirmSet.contains(serverIp)){
                deleteFromMap(productId);
            }
            if (productUpdatedConfirmSet.size() >= serverCount - 1) {
                deleteList.add(productId);
            } else {
                redisTemplate.<String, String>opsForHash().put(KEY_NAME + productId, serverIp, "");
            }
        }
        if (!deleteList.isEmpty()){
            updateHash.delete(KEY_NAME, deleteList);
        }
    }

    private void deleteFromMap(Long productId){
        ProductAllInOneWrapDto wrapDto = allInOneWrapDtoMap.get(productId);
        allInOneWrapDtoMap.remove(productId);
        wrapDto.getPrevious().setNext(wrapDto.getNext());
        wrapDto.getNext().setPrevious(wrapDto.getPrevious());
        if (wrapDto==last){
            last = null;
        }
        if (wrapDto==first){
            first = null;
        }
    }

    @Scheduled(cron = "0/1 * * * * *")
    private void addProductAllInOneInMap(){
        if (!tempList.isEmpty()){
            synchronized (tempList){
                while (!tempList.isEmpty()){
                    ProductAllInOneResponse productAllInOneResponse = tempList.get(0);
                    tempList.remove(0);
                    setWrapDtoToLast(productAllInOneResponse);
                }
                while(allInOneWrapDtoMap.size() > MAX_CACHE_NUM){
                    ProductAllInOneResponse removeTarget = first.getData();
                    allInOneWrapDtoMap.remove(removeTarget.getInfo().getId());
                    first.getNext().setPrevious(null);
                    first = first.getNext();
                }
            }
        }
    }

    private void setWrapDtoToLast(ProductAllInOneResponse productAllInOneResponse) {
        ProductAllInOneWrapDto wrapDto;
        if (allInOneWrapDtoMap.containsKey(productAllInOneResponse.getInfo().getId())){
            wrapDto = allInOneWrapDtoMap.get(productAllInOneResponse.getInfo().getId());
            setExistDtoToLast(wrapDto);
        }else {
            wrapDto = new ProductAllInOneWrapDto();
            wrapDto.setData(productAllInOneResponse);
            allInOneWrapDtoMap.put(wrapDto.getData().getInfo().getId(),wrapDto);
        }
        if (wrapDto != last){
            wrapDto.setPrevious(last);
            last.setNext(wrapDto);
            last = wrapDto;
        }
        if(first == null){
            first = wrapDto;
        }
    }

    private static void setExistDtoToLast(ProductAllInOneWrapDto wrapDto) {
        if (wrapDto.getNext() != null){
            if(wrapDto.getPrevious() != null){
                wrapDto.getPrevious().setNext(wrapDto.getNext());
            }
            wrapDto.getNext().setPrevious(wrapDto.getPrevious());
        }
    }

}
