package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuthorService{
  void createAuthor(CreateAuthorRequest author);

  void updateAuthor(UpdateAuthorRequest request);

  void deleteAuthor(Long id);

  Page<RetrieveAuthorResponse> retrieveAllAuthor(Pageable pageable);
}
