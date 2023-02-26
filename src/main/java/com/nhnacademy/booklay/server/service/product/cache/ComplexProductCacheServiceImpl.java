package com.nhnacademy.booklay.server.service.product.cache;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComplexProductCacheServiceImpl implements ComplexProductCacheService{
    private final ProductRecommendIdListCacheWrapService productRecommendIdListCacheWrapService;
    private final ProductResponseCacheWrapService productResponseCacheWrapService;
    @Override
    public List<RetrieveProductResponse> cacheRetrieveRecommendProducts(Long productId) throws IOException {
        List<Long> productIdList = productRecommendIdListCacheWrapService.cacheRetrieveRecommendProductIds(productId);
        return productResponseCacheWrapService.cacheRetrieveProductResponseList(productIdList);
    }

    @Override
    public List<SearchProductResponse> cacheRetrieveLatestProduct() throws IOException {
        List<Long> productIdList = productRecommendIdListCacheWrapService.cacheRetrieveRecommendProductIds(-1L);
        List<RetrieveProductResponse> productResponseList = productResponseCacheWrapService.cacheRetrieveProductResponseList(productIdList);
        return productResponseList.stream()
                        .map(SearchProductResponse::new)
                        .collect(Collectors.toList());
    }
}
