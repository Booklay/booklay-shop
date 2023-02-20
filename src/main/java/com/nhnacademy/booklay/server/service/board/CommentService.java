package com.nhnacademy.booklay.server.service.board;

import com.nhnacademy.booklay.server.dto.board.request.CommentRequest;
import com.nhnacademy.booklay.server.dto.board.response.CommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

  Page<CommentResponse> retrieveCommentPage(Long postId, Pageable pageable);
  Long createComment(CommentRequest request);

  Long updateComment(CommentRequest request);

  void deleteComment(Long commentId, Long memberNo);

}
