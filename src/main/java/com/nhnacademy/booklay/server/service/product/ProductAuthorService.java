package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.ProductAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAuthorService{
  ProductAuthor createProductAuthor(ProductAuthor productAuthor);

  void deleteProductAuthors(Long id);
}
