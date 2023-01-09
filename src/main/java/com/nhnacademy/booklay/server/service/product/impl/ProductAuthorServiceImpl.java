package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.service.product.ProductAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class ProductAuthorServiceImpl implements ProductAuthorService {

  private final ProductAuthorRepository productAuthorRepository;

  @Override
  @Transactional
  public ProductAuthor createProductAuthor(ProductAuthor productAuthor) {
    return productAuthorRepository.save(productAuthor);
  }

  @Override
  @Transactional
  public void deleteProductAuthors(Long id) {
    productAuthorRepository.deleteAllByProductDetailId(id);
  }
}
