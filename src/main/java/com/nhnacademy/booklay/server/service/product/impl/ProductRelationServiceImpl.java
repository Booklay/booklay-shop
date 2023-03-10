package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteProductRelationRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductRelation;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.product.ProductRelationRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.product.ProductRelationService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.PRODUCT_RECOMMEND_LIST;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
@Transactional
public class ProductRelationServiceImpl implements ProductRelationService {

  private final ProductRelationRepository productRelationRepository;
  private final ProductService productService;
  private final ProductRepository productRepository;
  private static final String PRODUCT_NOT_FOUND = "product not found";
  private final RedisCacheService redisCacheService;
  //productId를 통해서 연관 상품 목록 호출
  @Override
  public List<RetrieveProductResponse> retrieveRecommendProducts(Long productId)
      throws IOException {
    List<Long> recommendProductIds = retrieveRecommendProductIds(productId);
    return productService.retrieveProductResponses(recommendProductIds);
  }

  @Override
  public List<Long> retrieveRecommendProductIds(Long productId) {
    return productRelationRepository.findRecommendIdsByBaseProductId(productId);
  }

  @Override
  public Page<RetrieveProductResponse> retrieveRecommendConnection(Long productNo,
      Pageable pageable) throws IOException {

    Page<RetrieveProductResponse> response = productService.retrieveProductPage(pageable);

    List<RetrieveProductResponse> content = response.getContent();

    for (RetrieveProductResponse product : content) {
      product.setRecommend(productRelationRepository.existsByBaseAndTargetId(productNo, product.getProductId()));
    }

    return new PageImpl<>(content, response.getPageable(),
        response.getTotalElements());
  }

  @Override
  public void createProductRelation(CreateDeleteProductRelationRequest request) {
    Product baseProduct = productRepository.findById(request.getBaseId()).orElseThrow(()->new NotFoundException(Product.class, PRODUCT_NOT_FOUND));
    Product targetProduct = productRepository.findById(request.getTargetId()).orElseThrow(()->new NotFoundException(Product.class, PRODUCT_NOT_FOUND));
    ProductRelation productRelation = new ProductRelation(baseProduct, targetProduct);

    productRelationRepository.save(productRelation);
    redisCacheService.deleteCache(PRODUCT_RECOMMEND_LIST, request.getBaseId());
  }

  @Override
  public void deleteProductRelation(CreateDeleteProductRelationRequest request){
    Product baseProduct = productRepository.findById(request.getBaseId()).orElseThrow(()->new NotFoundException(Product.class, PRODUCT_NOT_FOUND));
    Product targetProduct = productRepository.findById(request.getTargetId()).orElseThrow(()->new NotFoundException(Product.class, PRODUCT_NOT_FOUND));
    productRelationRepository.deleteByBaseAndTargetId(baseProduct.getId(), targetProduct.getId());
    redisCacheService.deleteCache(PRODUCT_RECOMMEND_LIST, request.getBaseId());
  }
}
