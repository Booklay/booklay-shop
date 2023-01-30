package com.nhnacademy.booklay.server.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductViewResponse;
import com.nhnacademy.booklay.server.service.product.BookSubscribeService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 최규태
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final BookSubscribeService bookSubscribeService;

    @GetMapping
    public PageResponse<RetrieveProductResponse> retrieveProductPage(Pageable pageable)
        throws JsonProcessingException {
        Page<RetrieveProductResponse> response = productService.retrieveProductPage(pageable);
        return new PageResponse<>(response);
    }

    @GetMapping("/view/{productNo}")
    public RetrieveProductViewResponse retrieveDetailView(@PathVariable Long productNo) {
        return productService.retrieveProductView(productNo);
    }

    @GetMapping("/view/subscribe/{subscribeId}")
    public List<RetrieveProductResponse> retrieveSubscribedBooks(@PathVariable Long subscribeId) {
        return bookSubscribeService.retrieveBookSubscribe(subscribeId);
    }

    @GetMapping("/recommend/{productId}")
    public List<RetrieveProductResponse> retrieveRecommendProducts(@PathVariable Long productId){
        return productService.retrieveRecommendProducts(productId);
    }
}
