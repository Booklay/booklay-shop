package com.nhnacademy.booklay.server.controller.board;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.server.dto.board.request.BoardPostUpdateRequest;
import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.service.board.PostService;
import com.nhnacademy.booklay.server.service.board.cache.PostResponsePageCacheWrapService;
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
@RequestMapping("/board")
public class BoardController {

  private final PostService postService;
  private final PostResponsePageCacheWrapService postResponsePageCacheWrapService;

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

  /**
   * 게시글 수정
   * @param request
   * @return
   */
  @PutMapping
  public ResponseEntity<Long> updatePost(@RequestBody BoardPostUpdateRequest request) {
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
    Page<PostResponse> content = postResponsePageCacheWrapService.cacheRetrievePostResponsePage(productId, pageable);

    PageResponse<PostResponse> response = new PageResponse<>(content);

    return ResponseEntity.status(HttpStatus.OK)
        .body(response);
  }

  @GetMapping("/notice")
  public ResponseEntity<PageResponse<PostResponse>> retrieveNotice(Pageable pageable){
    Page<PostResponse> content = postService.retrieveNotice(pageable);

    PageResponse<PostResponse> response = new PageResponse<>(content);

    return ResponseEntity.status(HttpStatus.OK)
        .body(response);
  }

  /**
   * 게시글 조회
   * @param postId
   * @return
   */
  @GetMapping("/{postId}")
  public ResponseEntity<PostResponse> viewPost(@PathVariable Long postId) {
    PostResponse response = postService.retrievePostById(postId);

    return ResponseEntity.status(HttpStatus.OK)
        .body(response);
  }

  @PutMapping("/confirm/{postId}")
  public ResponseEntity<Long> confirmAnswer(@PathVariable Long postId){
    Long response = postService.updateConfirmAnswer(postId);
    return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
  }

  /**
   * 게시글 소프트 딜리트
   * @param memberInfo
   * @param postId
   * @return
   */
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(MemberInfo memberInfo, @PathVariable Long postId) {
    log.info("출력" + memberInfo.getMemberNo());
    postService.deletePost(memberInfo.getMemberNo(), postId);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }
}
