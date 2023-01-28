package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AuthorRepositoryCustom {
  Page<RetrieveAuthorResponse> findAllBy(Pageable pageable);
}
