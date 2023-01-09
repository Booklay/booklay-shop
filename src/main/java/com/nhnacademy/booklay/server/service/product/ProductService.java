package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.Product;

public interface ProductService {

  Product createProduct(Product product);

  Product updateProduct(Long id, Product product);

}
