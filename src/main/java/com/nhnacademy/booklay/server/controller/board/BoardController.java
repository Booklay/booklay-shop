package com.nhnacademy.booklay.server.controller.board;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.server.dto.board.request.BoardPostUpdateRequest;
import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import com.nhnacademy.booklay.server.service.board.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 최규태
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

  private final PostService postService;

  /**
   * 게시글 등록
   *
   * @param request
   * @return
   */
  @PostMapping
  public ResponseEntity<Long> createPost(@RequestBody BoardPostCreateRequest request) {
    Long result = postService.createPost(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(result);
  }

  @PutMapping
  public ResponseEntity<Long> updatePost(@RequestBody BoardPostUpdateRequest request){
    Long result = postService.updatePost(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(result);
  }

  /**
   * 상품 문의 게시글 페이지로 조회
   *
   * @param productId
   * @param pageable
   * @return
   */
  @GetMapping("/product/{productId}")
  public ResponseEntity<PageResponse<PostResponse>> retrieveProductQNA(@PathVariable Long productId,
      Pageable pageable) {
    Page<PostResponse> content = postService.retrieveProductQNA(productId, pageable);

    PageResponse<PostResponse> response = new PageResponse<>(content);

    return ResponseEntity.status(HttpStatus.OK)
        .body(response);
  }

  @GetMapping("/{postId}")
  public ResponseEntity<PostResponse> viewPost(@PathVariable Long postId) {
    PostResponse response = postService.retrievePostById(postId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(response);
  }
}
