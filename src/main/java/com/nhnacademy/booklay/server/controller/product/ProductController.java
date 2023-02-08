package com.nhnacademy.booklay.server.controller.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.product.BookSubscribeService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
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
  private final ProductRelationService productRelationService;

  /**
   * 조건 없이 전체 상품 리스트 페이지를 리턴
   *
   * @param pageable
   * @return
   */
  @GetMapping
  public PageResponse<ProductAllInOneResponse> getProductPage(Pageable pageable) {
    Page<ProductAllInOneResponse> response = productService.getProductsPage(pageable);
    return new PageResponse<>(response);
  }

  @GetMapping("/view/{productNo}")
  public ProductAllInOneResponse retrieveDetailView(@PathVariable Long productNo) {
    return productService.findProductById(productNo);
  }

  @GetMapping("/view/subscribe/{subscribeId}")
  public List<RetrieveProductResponse> retrieveSubscribedBooks(@PathVariable Long subscribeId)
      throws IOException {
    return bookSubscribeService.retrieveBookSubscribe(subscribeId);
  }

  @GetMapping("/recommend/{productId}")
  public List<RetrieveProductResponse> retrieveRecommendProducts(@PathVariable Long productId)
      throws IOException {
    return productRelationService.retrieveRecommendProducts(productId);
  }

}
