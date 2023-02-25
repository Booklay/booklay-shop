package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.cache.ObjectWrapDto;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.PRODUCT_RECOMMEND_LIST;

@Service
@RequiredArgsConstructor
public class ProductRecommendIdListCacheWrapServiceImpl implements ProductRecommendIdListCacheWrapService {
    private final RedisCacheService redisCacheService;
    private final ProductRelationService productRelationService;
    private final ProductService productService;
    private ObjectWrapDto<List<Long>> first;
    private ObjectWrapDto<List<Long>> last;
    private final Map<Long, ObjectWrapDto<List<Long>>> recommendProductIdListWrapDtoMap = new HashMap<>();
    //맵에 넣기 위한 대기열
    private final List<ObjectWrapDto<List<Long>>> tempList = Collections.synchronizedList(new LinkedList<>());
    private static final int MAX_CACHE_NUM = 200;

    @Override
    public List<Long> cacheRetrieveRecommendProductIds(Long productId){
        if (recommendProductIdListWrapDtoMap.containsKey(productId)){
            ObjectWrapDto<List<Long>> wrapDto = recommendProductIdListWrapDtoMap.get(productId);
            tempList.add(wrapDto);
            return wrapDto.getData();
        }else {
            List<Long> idList;
            if (productId == -1){
                idList = productService.retrieveLatestEightsIds();
            }else {
                idList =productRelationService.retrieveRecommendProductIds(productId);
            }
            ObjectWrapDto<List<Long>> wrapDto = new ObjectWrapDto<>();
            wrapDto.setData(idList);
            wrapDto.setKey(productId);
            tempList.add(wrapDto);
            return idList;
        }
    }

    @Override
    @Scheduled(fixedRate = 100)
    public void updateCheck(){
        List<Long> mapDeleteList = redisCacheService.updateCheck(PRODUCT_RECOMMEND_LIST);
        for (Long productNo : mapDeleteList){
            deleteFromMap(productNo);
        }

    }

    private void deleteFromMap(Long productId){
        ObjectWrapDto<List<Long>> wrapDto = recommendProductIdListWrapDtoMap.get(productId);
        recommendProductIdListWrapDtoMap.remove(productId);
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
                    ObjectWrapDto<List<Long>> wrapDto = tempList.get(0);
                    tempList.remove(0);
                    setWrapDtoToLast(wrapDto);
                }
            }
            while(recommendProductIdListWrapDtoMap.size() > MAX_CACHE_NUM){
                ObjectWrapDto<List<Long>> removeTarget = first;
                recommendProductIdListWrapDtoMap.remove(removeTarget.getKey());
                first.getNext().setPrevious(null);
                first = first.getNext();
            }
        }
    }

    private void setWrapDtoToLast(ObjectWrapDto<List<Long>> wrapDtoToLast) {
        if (recommendProductIdListWrapDtoMap.containsKey(wrapDtoToLast.getKey())){
            unlinkExistDto(wrapDtoToLast);
        }else {
            recommendProductIdListWrapDtoMap.put(wrapDtoToLast.getKey(),wrapDtoToLast);
        }
        if (last == null){
            last = wrapDtoToLast;
        }
        if (wrapDtoToLast != last){
            wrapDtoToLast.setPrevious(last);
            last.setNext(wrapDtoToLast);
            last = wrapDtoToLast;
        }
        if(first == null){
            first = wrapDtoToLast;
        }
    }

    private void unlinkExistDto(ObjectWrapDto<List<Long>> wrapDto) {
        if (wrapDto.getNext() != null){
            if(wrapDto.getPrevious() != null){
                wrapDto.getPrevious().setNext(wrapDto.getNext());
            }
            wrapDto.getNext().setPrevious(wrapDto.getPrevious());
        }
    }


}
