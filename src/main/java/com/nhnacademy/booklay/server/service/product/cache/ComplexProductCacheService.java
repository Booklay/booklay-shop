package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchProductResponse;

import java.io.IOException;
import java.util.List;

public interface ComplexProductCacheService {
    List<RetrieveProductResponse> cacheRetrieveRecommendProducts(Long productId) throws IOException;

    List<SearchProductResponse> cacheRetrieveLatestProduct() throws IOException;
}
