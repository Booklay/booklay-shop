package com.nhnacademy.booklay.server.service.search;

import java.util.List;

public interface SearchService {
    void saveAllDocuments();

    List<Long> retrieveProductsIdsByKeywords(String keywords);

    List<Long> retrieveCategoryHitsByIdMatch(String categoryId);

    List<Long> retrieveProductHitsByKeywordsMatch(String keywords);

    List<Long> retrieveProductsIdsByTags(String keywords);

    List<Long> retrieveProductsIdsByAuthors(String keywords);

    List<Long> retrieveProductsIdsByCategory(String keywords);
}
