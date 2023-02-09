package com.nhnacademy.booklay.server.repository.post;

import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PostRepositoryCustom {
  Page<PostResponse> findAllByProductIdPage(Long productId, Pageable pageable);
}
