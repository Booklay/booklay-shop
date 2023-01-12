package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.Author;

public interface AuthorService{
  Author createAuthor(Author author);

  Author retrieveAuthorById(Long id) throws Exception;
}
