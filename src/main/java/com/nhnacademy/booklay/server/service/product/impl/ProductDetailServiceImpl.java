package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.service.product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class ProductDetailServiceImpl implements ProductDetailService {

  private final ProductDetailRepository productDetailRepository;

  @Override
  @Transactional
  public ProductDetail createProductDetail(ProductDetail productDetail) {
    return productDetailRepository.save(productDetail);
  }

  @Override
  @Transactional
  public void updateProductDetail(ProductDetail productDetail) {
    if(!productDetailRepository.existsById(productDetail.getId())){
      throw new IllegalArgumentException();
    }
    productDetailRepository.save(productDetail);
  }
}
