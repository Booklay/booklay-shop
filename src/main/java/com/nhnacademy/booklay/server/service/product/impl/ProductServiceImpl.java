package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Override
  public Product createProduct(Product product) {
    return productRepository.save(product);
  }

  @Override
  public Product updateProduct(Long id, Product product) {
    return productRepository.updateProductById(id, product);
  }
}
