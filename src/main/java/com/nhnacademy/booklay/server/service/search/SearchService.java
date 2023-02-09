package com.nhnacademy.booklay.server.service.search;

import com.nhnacademy.booklay.server.dto.search.request.SearchKeywordsRequest;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchProductResponse;
import org.springframework.data.domain.Pageable;

public interface SearchService {

    void saveAllDocuments();

    SearchPageResponse<SearchProductResponse> getAllProducts(Pageable pageable);

    SearchPageResponse<SearchProductResponse> searchProductsByKeywords(SearchKeywordsRequest request, Pageable pageable);

    SearchPageResponse<SearchProductResponse> searchProductsByCategory(Long categoryId, Pageable pageable);
}
