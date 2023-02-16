package com.nhnacademy.booklay.server.repository.post;

import com.nhnacademy.booklay.server.dto.board.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommentRepositoryCustom {
  void updateUpperCommentByGroupNoCommentId(Long postId, Integer rebaseOrder);

  Integer countChildByGroupNo(Long groupNo);

  Page<CommentResponse> findAllByPostIdAndPageable(Long postId, Pageable pageable);

  Long softDelete(Long commentId);
}
