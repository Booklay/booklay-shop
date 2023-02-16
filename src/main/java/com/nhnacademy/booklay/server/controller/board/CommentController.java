package com.nhnacademy.booklay.server.controller.board;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.board.request.CommentRequest;
import com.nhnacademy.booklay.server.dto.board.response.CommentResponse;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.service.board.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 최규태
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

  private final CommentService commentService;

  @GetMapping("/{postId}")
  public ResponseEntity<PageResponse<CommentResponse>> retrieveCommentPage(
      @PathVariable Long postId, Pageable pageable) {
    Page<CommentResponse> result = commentService.retrieveCommentPage(postId, pageable);
    PageResponse<CommentResponse> page = new PageResponse(result);
    return ResponseEntity.status(HttpStatus.OK).body(page);
  }

  @PostMapping
  public ResponseEntity createComment(@RequestBody CommentRequest request) {
    Long result = commentService.createComment(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(result);
  }

  @PutMapping
  public ResponseEntity updateComment(@RequestBody CommentRequest request) {
    Long result = commentService.updateComment(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(result);
  }

  @DeleteMapping("/{commentId}")
  public ResponseEntity deleteComment(@PathVariable Long commentId, MemberInfo memberInfo) {
    commentService.deleteComment(commentId, memberInfo.getMemberNo());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
