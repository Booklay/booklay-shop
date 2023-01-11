package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorService{
  Author createAuthor(Author author);

  Author retrieveAuthorById(Long id) throws Exception;
}
