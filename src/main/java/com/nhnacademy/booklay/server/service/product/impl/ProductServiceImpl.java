package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Override
  public Product createProduct(Product product) {
    return productRepository.save(product);
  }

  @Override
  @Transactional
  public Product updateProduct(Long id, Product product) {
    productRepository.updateProductById(id, product);
    return productRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("not found"));
  }

}
