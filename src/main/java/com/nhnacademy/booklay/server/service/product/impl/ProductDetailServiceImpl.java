package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

  private final ProductDetailRepository productDetailRepository;

  @Override
  public ProductDetail createProductDetail(ProductDetail productDetail) {
    return productDetailRepository.save(productDetail);
  }

  @Override
  public ProductDetail updateProductDetail(Long id, ProductDetail productDetail) {
    return productDetailRepository.updateProductDetailById(id, productDetail);
  }
}
