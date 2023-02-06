package com.nhnacademy.booklay.server.controller.search;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.search.request.SearchRequest;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.search.SearchService;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final ProductService productService;

    public SearchController(SearchService searchService, ProductService productService) {
        this.searchService = searchService;
        this.productService = productService;
    }

    @GetMapping("/save/all")
    public ResponseEntity<Void> saveAll() {
        searchService.saveAllDocuments();
        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    @PostMapping("/products")
    public ResponseEntity<PageResponse<ProductAllInOneResponse>> search(@Valid @RequestBody SearchRequest searchRequest,
                                                                        Pageable pageable) {

        List<Long> productIds = resolveRequest(searchRequest);

        Page<ProductAllInOneResponse> page = productService.retrieveProductListByProductNoList(productIds, pageable);

        PageResponse<ProductAllInOneResponse> pageResponse = new PageResponse<>(page);

        return ResponseEntity.status(HttpStatus.OK)
            .body(pageResponse);
    }

    private List<Long> resolveRequest(SearchRequest searchRequest) {

        return searchService.retrieveProductsIdsByKeywords(searchRequest.getKeywords());
    }

}
