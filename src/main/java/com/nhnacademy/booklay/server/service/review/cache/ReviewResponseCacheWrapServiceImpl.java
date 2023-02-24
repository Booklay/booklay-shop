package com.nhnacademy.booklay.server.service.review.cache;

import com.nhnacademy.booklay.server.dto.review.cache.ReviewResponseWrapDto;
import com.nhnacademy.booklay.server.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.REVIEW_PAGE_KEY_NAME;

@Service
@RequiredArgsConstructor
public class ReviewResponseCacheWrapServiceImpl implements ReviewResponsePageCacheWrapService {
    private final RedisCacheService redisCacheService;
    private final ReviewService reviewService;
    private ReviewResponseWrapDto first;
    private ReviewResponseWrapDto last;
    private final Map<Long, ReviewResponseWrapDto> reviewResponseWrapDtoHashMap = new HashMap<>();
    //맵에 넣기 위한 대기열
    private final List<ReviewResponseWrapDto> tempList = Collections.synchronizedList(new LinkedList<>());
    @Qualifier("serverIp")
    private final String serverIp;
    private static final int MAX_CACHE_NUM = 200;

    @Override
    public SearchPageResponse<RetrieveReviewResponse> cacheRetrievePostResponsePage(Long productId,
                                                                                    Pageable pageable){
        if (pageable.getPageNumber() == 0){
            ReviewResponseWrapDto wrapDto = reviewResponseWrapDtoHashMap.get(productId);
            SearchPageResponse<RetrieveReviewResponse>
                response = wrapDto==null?
                reviewService.retrieveReviewListByProductId(productId, pageable):wrapDto.getData();
            ReviewResponseWrapDto postResponseWrapDto = new ReviewResponseWrapDto();
            postResponseWrapDto.setData(response);
            postResponseWrapDto.setProductId(productId);
            tempList.add(postResponseWrapDto);
            return response;
        }else {
            return reviewService.retrieveReviewListByProductId(productId, pageable);
        }
    }

    @Override
    @Scheduled(fixedRate = 100)
    public void updateCheck(){
        List<Long> mapDeleteList = redisCacheService.updateCheck(REVIEW_PAGE_KEY_NAME, reviewResponseWrapDtoHashMap);
        for (Long productNo:mapDeleteList){
            deleteFromMap(productNo);
        }

    }

    private void deleteFromMap(Long productId){
        ReviewResponseWrapDto wrapDto = reviewResponseWrapDtoHashMap.get(productId);
        reviewResponseWrapDtoHashMap.remove(productId);
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
                    ReviewResponseWrapDto productAllInOneResponse = tempList.get(0);
                    tempList.remove(0);
                    setWrapDtoToLast(productAllInOneResponse);
                }
            }
            while(reviewResponseWrapDtoHashMap.size() > MAX_CACHE_NUM){
                ReviewResponseWrapDto removeTarget = first;
                reviewResponseWrapDtoHashMap.remove(removeTarget.getProductId());
                first.getNext().setPrevious(null);
                first = first.getNext();
            }
        }
    }

    private void setWrapDtoToLast(ReviewResponseWrapDto productAllInOneResponse) {
        ReviewResponseWrapDto wrapDto;
        if (reviewResponseWrapDtoHashMap.containsKey(productAllInOneResponse.getProductId())){
            wrapDto = reviewResponseWrapDtoHashMap.get(productAllInOneResponse.getProductId());
            setExistDtoToLast(wrapDto);
        }else {
            wrapDto = productAllInOneResponse;
            reviewResponseWrapDtoHashMap.put(wrapDto.getProductId(),wrapDto);
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

    private static void setExistDtoToLast(ReviewResponseWrapDto wrapDto) {
        if (wrapDto.getNext() != null){
            if(wrapDto.getPrevious() != null){
                wrapDto.getPrevious().setNext(wrapDto.getNext());
            }
            wrapDto.getNext().setPrevious(wrapDto.getPrevious());
        }
    }

}
