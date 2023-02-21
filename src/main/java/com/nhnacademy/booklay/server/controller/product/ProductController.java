package com.nhnacademy.booklay.server.controller.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.search.request.SearchIdRequest;
import com.nhnacademy.booklay.server.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.server.dto.search.response.SearchProductResponse;
import com.nhnacademy.booklay.server.service.product.BookSubscribeService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  private final ProductRelationService productRelationService;

  /**
   * 조건 없이 전체 상품 리스트 페이지를 리턴
   *
   * @param pageable
   * @return
   */
  @GetMapping
  public ResponseEntity<PageResponse<ProductAllInOneResponse>> getProductPage(Pageable pageable) {
    Page<ProductAllInOneResponse> response = productService.getProductsPage(pageable);
    PageResponse<ProductAllInOneResponse> body = new PageResponse<>(response);
    return ResponseEntity.status(HttpStatus.OK).body(body);
  }

  @GetMapping("/view/{productNo}")
  public ResponseEntity<ProductAllInOneResponse> retrieveDetailView(@PathVariable Long productNo) {
    ProductAllInOneResponse result = productService.findProductById(productNo);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping("/view/subscribe/{subscribeId}")
  public ResponseEntity<List<RetrieveProductResponse>> retrieveSubscribedBooks(@PathVariable Long subscribeId)
      throws IOException {
    List<RetrieveProductResponse> result = bookSubscribeService.retrieveBookSubscribe(subscribeId);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping("/recommend/{productId}")
  public ResponseEntity<List<RetrieveProductResponse>> retrieveRecommendProducts(@PathVariable Long productId)
      throws IOException {
    List<RetrieveProductResponse> result = productRelationService.retrieveRecommendProducts(productId);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  @GetMapping("/all")
  public ResponseEntity<SearchPageResponse<SearchProductResponse>> searchAll(Pageable pageable) {

    SearchPageResponse<SearchProductResponse> pageResponse = productService.getAllProducts(
        pageable);

    return ResponseEntity.status(HttpStatus.OK)
        .body(pageResponse);
  }

  @GetMapping("/latest")
  public ResponseEntity<List<SearchProductResponse>> getLatestProduct() {

    List<SearchProductResponse> pageResponse = productService.getLatestEights();

    return ResponseEntity.status(HttpStatus.OK)
        .body(pageResponse);
  }

  @PostMapping("/request")
  public ResponseEntity<SearchPageResponse<SearchProductResponse>> searchByRequest(
      @RequestBody SearchIdRequest request, Pageable pageable) {

    SearchPageResponse<SearchProductResponse> pageResponse =
        productService.retrieveProductByRequest(request,
            PageRequest.of(pageable.getPageNumber(), 16));

    return ResponseEntity.status(HttpStatus.OK)
        .body(pageResponse);
  }

}
