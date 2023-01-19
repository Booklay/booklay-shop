package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author 최규태
 */

public interface AuthorService {

  void createAuthor(CreateAuthorRequest author);

  void updateAuthor(UpdateAuthorRequest request);

  void deleteAuthor(DeleteIdRequest request);

  Page<RetrieveAuthorResponse> retrieveAllAuthor(Pageable pageable);
}
