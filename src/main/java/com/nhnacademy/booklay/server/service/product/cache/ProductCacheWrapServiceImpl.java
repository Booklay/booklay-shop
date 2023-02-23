package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.cache.ProductAllInOneWrapDto;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final RedisCacheService redisCacheService;
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
    @Override
    @Scheduled(cron = "0/1 * * * * *")
    public void updateCheck(){
        List<Long> mapDeleteList = redisCacheService.updateCheck(KEY_NAME, allInOneWrapDtoMap);
        for (Long productNo : mapDeleteList){
            deleteFromMap(productNo);
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
