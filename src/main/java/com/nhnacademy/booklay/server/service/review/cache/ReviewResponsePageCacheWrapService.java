package com.nhnacademy.booklay.server.service.review.cache;

import com.nhnacademy.booklay.server.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;

public interface ReviewResponsePageCacheWrapService {

    SearchPageResponse<RetrieveReviewResponse> cacheRetrievePostResponsePage(Long productId, Pageable pageable);

    @Scheduled(cron = "0/1 * * * * *")
    void updateCheck();
}
