package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import org.springframework.scheduling.annotation.Scheduled;

public interface ProductCacheWrapService {
    ProductAllInOneResponse cacheRetrieveProductAllInOne(Long productId);

    @Scheduled(cron = "0/1 * * * * *")
    void updateCheck();
}
