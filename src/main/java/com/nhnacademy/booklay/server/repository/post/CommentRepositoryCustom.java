package com.nhnacademy.booklay.server.repository.post;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommentRepositoryCustom {
  void updateUpperCommentByGroupNoCommentId(Long postId, Integer rebaseOrder);

  Integer countChildByGroupNo(Long groupNo);
}
