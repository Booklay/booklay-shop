package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.ProductAuthor;

public interface ProductAuthorService{
  ProductAuthor createProductAuthor(ProductAuthor productAuthor);

  void deleteProductAuthors(Long id);
}
