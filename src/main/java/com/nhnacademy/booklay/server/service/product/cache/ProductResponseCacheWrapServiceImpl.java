package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.cache.ProductResponseWrapDto;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
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
public class ProductResponseCacheWrapServiceImpl implements ProductResponseCacheWrapService {
    @Qualifier("StringTypeRedisTemplate")
    private final RedisTemplate<String, String> redisTemplate;
    private final ProductService productService;
    private ProductResponseWrapDto first;
    private ProductResponseWrapDto last;
    private final Map<Long, ProductResponseWrapDto> productResponseWrapDtoMap = new HashMap<>();
    //맵에 넣기 위한 대기열
    private final List<RetrieveProductResponse> tempList = Collections.synchronizedList(new LinkedList<>());
    @Qualifier("serverIp")
    private final String serverIp;
    private static final int MAX_CACHE_NUM = 200;


    @Override
    public List<RetrieveProductResponse> cacheRetrieveProductResponseList(List<Long> productIdList)
        throws IOException {
        List<Long> listForFind = new ArrayList<>();
        List<Integer> pointCheck = new ArrayList<>();
        List<RetrieveProductResponse> response = new ArrayList<>();
        for (int i = 0; i<productIdList.size(); i++){
            ProductResponseWrapDto wrapDto = productResponseWrapDtoMap.get(productIdList.get(i));
            if (wrapDto == null){
                pointCheck.add(i);
                listForFind.add(productIdList.get(i));
                response.add(null);
            }else {
                response.add(wrapDto.getData());
            }
        }
        List<RetrieveProductResponse> searchList = productService.retrieveProductResponses(listForFind);
        for (int i = 0; i<searchList.size(); i++){
            response.set(pointCheck.get(i),searchList.get(i));
        }
        tempList.addAll(searchList);
        return response;
    }



    private static final String KEY_NAME = "productResponseCache";
    private static final String KEY_NAME_SERVER_COUNT = "productResponseCacheServerCheck";
    @Override
    @Scheduled(cron = "0/1 * * * * *")
    public void updateCheck(){
        HashOperations<String, Long, List<String>> updateHash = redisTemplate.opsForHash();
        redisTemplate.<String, String>opsForHash().put(KEY_NAME_SERVER_COUNT, serverIp, LocalDateTime.now().toString());
        long serverCount = updateHash.size(KEY_NAME_SERVER_COUNT);
        Set<Long> changedProductSet = updateHash.keys(KEY_NAME);
        changedProductSet.stream().filter(productResponseWrapDtoMap::containsKey).forEach(this::deleteFromMap);
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
        ProductResponseWrapDto wrapDto = productResponseWrapDtoMap.get(productId);
        productResponseWrapDtoMap.remove(productId);
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
                    RetrieveProductResponse productAllInOneResponse = tempList.get(0);
                    tempList.remove(0);
                    setWrapDtoToLast(productAllInOneResponse);
                }
                while(productResponseWrapDtoMap.size() > MAX_CACHE_NUM){
                    RetrieveProductResponse removeTarget = first.getData();
                    productResponseWrapDtoMap.remove(removeTarget.getProductId());
                    first.getNext().setPrevious(null);
                    first = first.getNext();
                }
            }
        }
    }

    private void setWrapDtoToLast(RetrieveProductResponse productAllInOneResponse) {
        ProductResponseWrapDto wrapDto;
        if (productResponseWrapDtoMap.containsKey(productAllInOneResponse.getProductId())){
            wrapDto = productResponseWrapDtoMap.get(productAllInOneResponse.getProductId());
            setExistDtoToLast(wrapDto);
        }else {
            wrapDto = new ProductResponseWrapDto();
            wrapDto.setData(productAllInOneResponse);
            productResponseWrapDtoMap.put(wrapDto.getData().getProductId(),wrapDto);
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

    private static void setExistDtoToLast(ProductResponseWrapDto wrapDto) {
        if (wrapDto.getNext() != null){
            if(wrapDto.getPrevious() != null){
                wrapDto.getPrevious().setNext(wrapDto.getNext());
            }
            wrapDto.getNext().setPrevious(wrapDto.getPrevious());
        }
    }

}
