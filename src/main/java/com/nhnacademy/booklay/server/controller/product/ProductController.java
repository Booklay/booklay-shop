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
import com.nhnacademy.booklay.server.service.product.cache.ComplexProductCacheService;
import com.nhnacademy.booklay.server.service.product.cache.ProductAllInOneCacheWrapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
  private final ProductAllInOneCacheWrapService productAllInOneCacheWrapService;
  private final ComplexProductCacheService complexProductCacheService;
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

  /**
   * 상품 상세 페이지 조회
   * @param productNo
   * @return
   */
  @GetMapping("/view/{productNo}")
  public ResponseEntity<ProductAllInOneResponse> retrieveDetailView(@PathVariable Long productNo) {
    ProductAllInOneResponse result = productAllInOneCacheWrapService.cacheRetrieveProductAllInOne(productNo);

    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  /**
   * 구독 하위 상품 목록 조회
   * @param subscribeId
   * @return
   * @throws IOException
   */
  @GetMapping("/view/subscribe/{subscribeId}")
  public ResponseEntity<List<RetrieveProductResponse>> retrieveSubscribedBooks(
      @PathVariable Long subscribeId)
      throws IOException {
    List<RetrieveProductResponse> result = bookSubscribeService.retrieveBookSubscribe(subscribeId);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  /**
   * 연관 상품 목록 조회
   * @param productId
   * @return
   * @throws IOException
   */
  @GetMapping("/recommend/{productId}")
  public ResponseEntity<List<RetrieveProductResponse>> retrieveRecommendProducts(
      @PathVariable Long productId)
      throws IOException {

    List<RetrieveProductResponse> result =
            complexProductCacheService.cacheRetrieveRecommendProducts(productId);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  /**
   * 상품 전체 조회
   * @param pageable
   * @return
   */
  @GetMapping("/all")
  public ResponseEntity<SearchPageResponse<SearchProductResponse>> searchAll(Pageable pageable) {

    SearchPageResponse<SearchProductResponse> pageResponse = productService.getAllProducts(
        pageable);

    return ResponseEntity.status(HttpStatus.OK)
        .body(pageResponse);
  }

  /**
   * 최근 등록 상품 조회
   * @return
   */
  @GetMapping("/latest")
  public ResponseEntity<List<SearchProductResponse>> getLatestProduct() throws IOException {

    List<SearchProductResponse> pageResponse = complexProductCacheService.cacheRetrieveLatestProduct();

    return ResponseEntity.status(HttpStatus.OK)
        .body(pageResponse);
  }

  /**
   * 검색 조회
   * @param request
   * @param pageable
   * @return
   */
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
