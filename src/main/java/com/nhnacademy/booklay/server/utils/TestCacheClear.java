package com.nhnacademy.booklay.server.utils;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestCacheClear {
    private final List<CacheServiceExtend<?>> cacheServiceExtendList;

    public void cacheDelete(){
        for(CacheServiceExtend<?> cacheServiceExtend :cacheServiceExtendList){
            log.info("{}{}", cacheServiceExtend.keyName, cacheServiceExtend.wrapDtoMap.size());
            cacheServiceExtend.wrapDtoMap.clear();
            log.info("{}{}", cacheServiceExtend.keyName, cacheServiceExtend.wrapDtoMap.size());
        }
    }
}
