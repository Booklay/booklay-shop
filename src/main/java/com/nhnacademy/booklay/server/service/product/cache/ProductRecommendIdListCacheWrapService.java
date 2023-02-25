package com.nhnacademy.booklay.server.service.product.cache;

import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface ProductRecommendIdListCacheWrapService {

    List<Long> cacheRetrieveRecommendProductIds(Long productId);

    @Scheduled(fixedRate = 100)
    void updateCheck();
}
