package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.service.product.ProductAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class ProductAuthorServiceImpl implements ProductAuthorService {

  private final ProductAuthorRepository productAuthorRepository;

  @Override
  public ProductAuthor createProductAuthor(ProductAuthor productAuthor) {
    return productAuthorRepository.save(productAuthor);
  }

  @Override
  public void deleteProductAuthors(Long id) {
    productAuthorRepository.deleteAllByProductDetailId(id);
  }
}
