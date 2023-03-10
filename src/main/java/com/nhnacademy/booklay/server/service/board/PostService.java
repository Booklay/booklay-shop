package com.nhnacademy.booklay.server.service.board;

import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.server.dto.board.request.BoardPostUpdateRequest;
import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

  Long createPost(BoardPostCreateRequest request);
  Page<PostResponse> retrieveProductQNA(Long productId, Pageable pageable);
  Page<PostResponse> retrieveNotice(Pageable pageable);
  PostResponse retrievePostById(Long postId);
  Long updatePost(BoardPostUpdateRequest request);
  void deletePost(Long memberId, Long postId);
  Long updateConfirmAnswer(Long postId);
  List<PostResponse> retrieveNoticeList(Integer pageLimit);
}
