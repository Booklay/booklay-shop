package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.ProductAuthor;

/**
 * @author 최규태
 */

public interface ProductAuthorService{
  ProductAuthor createProductAuthor(ProductAuthor productAuthor);

  void deleteProductAuthors(Long id);
}
