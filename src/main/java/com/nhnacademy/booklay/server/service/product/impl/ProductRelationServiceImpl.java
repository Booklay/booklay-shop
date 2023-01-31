package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.repository.product.ProductRelationRepository;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductRelationServiceImpl implements ProductRelationService {

  private final ProductRelationRepository productRelationRepository;
  private final ProductService productService;

  //productId를 통해서 연관 상품 목록 호출
  @Override
  public List<RetrieveProductResponse> retrieveRecommendProducts(Long productId) {

    List<Long> recommendProductIds = productRelationRepository.findRecommendIdsByBaseProductId(productId);

    return productService.retrieveProductResponses(recommendProductIds);
  }
}
