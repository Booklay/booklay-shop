package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;

public interface ProductResponseCacheWrapService {

    List<RetrieveProductResponse> cacheRetrieveProductResponseList(List<Long> productIdList)
        throws IOException;

    @Scheduled(fixedRate = 100)
    void updateCheck();
}
