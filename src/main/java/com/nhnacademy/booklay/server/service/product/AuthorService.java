package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService{
  Author createAuthor(Author author);

  Author retrieveAuthorById(Long id) throws Exception;

  Page<RetrieveAuthorResponse> retrieveAllAuthor(Pageable pageable);
}
