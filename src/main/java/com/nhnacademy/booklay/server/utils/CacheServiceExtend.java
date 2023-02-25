package com.nhnacademy.booklay.server.utils;

import com.nhnacademy.booklay.server.dto.product.cache.ObjectWrapDto;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
public abstract class CacheServiceExtend<T> {
    private final RedisCacheService redisCacheService;
    private ObjectWrapDto<T> first;
    private ObjectWrapDto<T> last;
    protected final Map<Long, ObjectWrapDto<T>> wrapDtoMap = new HashMap<>();
    //맵에 넣기 위한 대기열
    protected final List<ObjectWrapDto<T> >tempList = Collections.synchronizedList(new LinkedList<>());
    private static final int MAX_CACHE_NUM = 200;
    private final String keyName;

    @Scheduled(fixedRate = 100)
    public void updateCheck(){
        List<Long> mapDeleteList = redisCacheService.updateCheck(keyName);
        for (Long productNo : mapDeleteList){
            deleteFromMap(productNo);
        }

    }

    private void deleteFromMap(Long productId){
        ObjectWrapDto<T> wrapDto = wrapDtoMap.get(productId);
        wrapDtoMap.remove(productId);
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
                    ObjectWrapDto<T> wrapDto = tempList.get(0);
                    tempList.remove(0);
                    setWrapDtoToLast(wrapDto);
                }
            }
            while(wrapDtoMap.size() > MAX_CACHE_NUM){
                ObjectWrapDto<T> removeTarget = first;
                wrapDtoMap.remove(removeTarget.getKey());
                first.getNext().setPrevious(null);
                first = first.getNext();
            }
        }
    }

    private void setWrapDtoToLast(ObjectWrapDto<T> wrapDtoToLast) {
        if (wrapDtoMap.containsKey(wrapDtoToLast.getKey())){
            unlinkExistDto(wrapDtoToLast);
        }else {
            wrapDtoMap.put(wrapDtoToLast.getKey(),wrapDtoToLast);
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

    private void unlinkExistDto(ObjectWrapDto<T> wrapDto) {
        if (wrapDto.getNext() != null){
            if(wrapDto.getPrevious() != null){
                wrapDto.getPrevious().setNext(wrapDto.getNext());
            }
            wrapDto.getNext().setPrevious(wrapDto.getPrevious());
        }
    }
}
