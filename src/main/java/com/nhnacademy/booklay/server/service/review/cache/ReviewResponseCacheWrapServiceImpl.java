package com.nhnacademy.booklay.server.service.review.cache;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.REVIEW_PAGE_KEY_NAME;

import com.nhnacademy.booklay.server.dto.product.cache.ObjectWrapDto;
import com.nhnacademy.booklay.server.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.review.ReviewService;
import com.nhnacademy.booklay.server.utils.CacheServiceExtend;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ReviewResponseCacheWrapServiceImpl extends CacheServiceExtend<SearchPageResponse<RetrieveReviewResponse>>
    implements ReviewResponsePageCacheWrapService {
    private final ReviewService reviewService;

    public ReviewResponseCacheWrapServiceImpl(RedisCacheService redisCacheService,
                                              ReviewService reviewService) {
        super(redisCacheService, REVIEW_PAGE_KEY_NAME);
        this.reviewService = reviewService;
    }
    @Override
    public SearchPageResponse<RetrieveReviewResponse> cacheRetrievePostResponsePage(Long productId,
                                                                                    Pageable pageable){
        if (pageable.getPageNumber() == 0){
            ObjectWrapDto<SearchPageResponse<RetrieveReviewResponse>> wrapDto = wrapDtoMap.get(productId);
            if (wrapDto == null){
                SearchPageResponse<RetrieveReviewResponse> response =
                    reviewService.retrieveReviewListByProductId(productId, pageable);
                wrapDto = new ObjectWrapDto<>();
                wrapDto.setData(response);
                wrapDto.setKey(productId);
                tempList.add(wrapDto);
            }
            return wrapDto.getData();
        }else {
            return reviewService.retrieveReviewListByProductId(productId, pageable);
        }
    }


}
