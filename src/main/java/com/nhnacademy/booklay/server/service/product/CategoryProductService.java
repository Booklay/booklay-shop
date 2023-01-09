package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.CategoryProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryProductService{
  CategoryProduct createCategoryProduct(CategoryProduct categoryProduct);

  void deleteAllByProductId(Long id);
}
