package com.nhnacademy.booklay.server.service.board.cache;

import com.nhnacademy.booklay.server.dto.board.cache.PostResponseWrapDto;
import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.board.PostService;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.POST_RESPONSE_PAGE_CACHE;

@Service
@RequiredArgsConstructor
public class PostResponsePageCacheWrapServiceImpl implements PostResponsePageCacheWrapService {
    private final RedisCacheService redisCacheService;
    private final PostService postService;
    private PostResponseWrapDto first;
    private PostResponseWrapDto last;
    private final Map<Long, PostResponseWrapDto>
            postResponseWrapDtoHashMap = new HashMap<>();
    //맵에 넣기 위한 대기열
    private final List<PostResponseWrapDto> tempList = Collections.synchronizedList(new LinkedList<>());
    @Qualifier("serverIp")
    private final String serverIp;
    private static final int MAX_CACHE_NUM = 200;

    @Override
    public Page<PostResponse> cacheRetrievePostResponsePage(Long productId,
                                                            Pageable pageable){
        if (pageable.getPageNumber() == 0){
            PostResponseWrapDto wrapDto = postResponseWrapDtoHashMap.get(productId);
            Page<PostResponse>
                response = wrapDto==null?postService.retrieveProductQNA(productId, pageable):wrapDto.getData();
            PostResponseWrapDto postResponseWrapDto = new PostResponseWrapDto();
            postResponseWrapDto.setData(response);
            postResponseWrapDto.setProductId(productId);
            tempList.add(postResponseWrapDto);
            return response;
        }else {
            return postService.retrieveProductQNA(productId, pageable);
        }
    }



    @Override
    @Scheduled(fixedRate = 100)
    public void updateCheck(){
        List<Long> mapDeleteList = redisCacheService.updateCheck(POST_RESPONSE_PAGE_CACHE, postResponseWrapDtoHashMap);
        for (Long productNo : mapDeleteList){
            deleteFromMap(productNo);
        }
    }

    private void deleteFromMap(Long productId){
        PostResponseWrapDto wrapDto = postResponseWrapDtoHashMap.get(productId);
        postResponseWrapDtoHashMap.remove(productId);
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
                    PostResponseWrapDto productAllInOneResponse = tempList.get(0);
                    tempList.remove(0);
                    setWrapDtoToLast(productAllInOneResponse);
                }
                while(postResponseWrapDtoHashMap.size() > MAX_CACHE_NUM){
                    PostResponseWrapDto removeTarget = first;
                    postResponseWrapDtoHashMap.remove(removeTarget.getProductId());
                    first.getNext().setPrevious(null);
                    first = first.getNext();
                }
            }
        }
    }

    private void setWrapDtoToLast(PostResponseWrapDto productAllInOneResponse) {
        PostResponseWrapDto wrapDto;
        if (postResponseWrapDtoHashMap.containsKey(productAllInOneResponse.getProductId())){
            wrapDto = postResponseWrapDtoHashMap.get(productAllInOneResponse.getProductId());
            setExistDtoToLast(wrapDto);
        }else {
            wrapDto = productAllInOneResponse;
            postResponseWrapDtoHashMap.put(wrapDto.getProductId(),wrapDto);
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

    private static void setExistDtoToLast(PostResponseWrapDto wrapDto) {
        if (wrapDto.getNext() != null){
            if(wrapDto.getPrevious() != null){
                wrapDto.getPrevious().setNext(wrapDto.getNext());
            }
            wrapDto.getNext().setPrevious(wrapDto.getPrevious());
        }
    }

}
