package com.nhnacademy.booklay.server.controller.search;

import com.nhnacademy.booklay.server.dto.search.request.SearchCategoryRequest;
import com.nhnacademy.booklay.server.dto.search.request.SearchKeywordsRequest;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchProductResponse;
import com.nhnacademy.booklay.server.service.search.SearchService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/products")
    public ResponseEntity<SearchPageResponse<SearchProductResponse>> searchAll(Pageable pageable) {

        SearchPageResponse<SearchProductResponse> pageResponse = searchService.getAllProducts(pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(pageResponse);
    }

    @GetMapping("/products/latest")
    public ResponseEntity<List<SearchProductResponse>> searchRecently() {

        List<SearchProductResponse> latestProducts = searchService.getLatestProducts();

        return ResponseEntity.status(HttpStatus.OK)
            .body(latestProducts);
    }

    @PostMapping("/products")
    public ResponseEntity<SearchPageResponse<SearchProductResponse>> searchProductsByKeywords(@Valid @RequestBody
                                                                                        SearchKeywordsRequest request, Pageable pageable) {

        SearchPageResponse<SearchProductResponse> pageResponse = searchService.searchProductsByKeywords(request, pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(pageResponse);
    }
    @PostMapping("/products/category")
    public ResponseEntity<SearchPageResponse<SearchProductResponse>> searchProductsByCategory(@Valid @RequestBody SearchCategoryRequest request, Pageable pageable) {

        SearchPageResponse<SearchProductResponse> pageResponse = searchService.searchProductsByCategory(
            request.getCategoryId(), pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(pageResponse);
    }

    @GetMapping("/save/all")
    public ResponseEntity<Void> saveAll() {

        searchService.saveAllDocuments();

        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

}
