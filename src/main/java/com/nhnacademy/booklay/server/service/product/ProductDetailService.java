package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

public interface ProductDetailService{
  ProductDetail createProductDetail(ProductDetail productDetail);

  ProductDetail updateProductDetail(Long id, ProductDetail productDetail);
}
