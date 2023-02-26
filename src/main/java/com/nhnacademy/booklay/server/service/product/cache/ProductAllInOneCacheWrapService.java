package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import org.springframework.scheduling.annotation.Scheduled;

public interface ProductAllInOneCacheWrapService {
    ProductAllInOneResponse cacheRetrieveProductAllInOne(Long productId);

    @Scheduled(fixedRate = 100)
    void updateCheck();
}
