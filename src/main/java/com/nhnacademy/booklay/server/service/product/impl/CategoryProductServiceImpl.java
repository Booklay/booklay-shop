package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.repository.CategoryProductRepository;
import com.nhnacademy.booklay.server.service.product.CategoryProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class CategoryProductServiceImpl implements CategoryProductService {

  private final CategoryProductRepository categoryProductRepository;

  @Override
  @Transactional
  public CategoryProduct createCategoryProduct(CategoryProduct categoryProduct) {
    return categoryProductRepository.save(categoryProduct);
  }

  @Override
  @Transactional
  public void deleteAllByProductId(Long id) {
    categoryProductRepository.deleteAllByProductId(id);
  }
}
