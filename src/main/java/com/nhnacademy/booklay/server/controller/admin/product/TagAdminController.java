package com.nhnacademy.booklay.server.controller.admin.product;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.product.DeleteIdRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.TagProductResponse;
import com.nhnacademy.booklay.server.service.product.TagService;
import javax.validation.Valid;
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
@RequestMapping("/admin/tag")
@RequiredArgsConstructor
public class TagAdminController {

  private final TagService tagService;

  /**
   * 태그 자체만 호출
   * @param pageable
   * @return
   */
  @GetMapping
  public ResponseEntity<PageResponse<RetrieveTagResponse>> tagPage(Pageable pageable) {
    Page<RetrieveTagResponse> response = tagService.retrieveAllTag(pageable);
    PageResponse<RetrieveTagResponse> result = new PageResponse<>(response);
    return ResponseEntity.status(HttpStatus.OK)
        .body(result);
  }

  /**
   * 태그 등록
   * @param request
   * @return
   */
  @PostMapping
  public ResponseEntity<Void> tagRegister(@Valid @RequestBody CreateTagRequest request) {
    tagService.createTag(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 태그 수정
   * @param request
   * @return
   */
  @PutMapping
  public ResponseEntity<Void> tagUpdate(@Valid @RequestBody UpdateTagRequest request) {
    tagService.updateTag(request);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  /**
   * 태그 삭제
   * @param id
   * @return
   */
  @DeleteMapping
  public ResponseEntity<Void> tagDelete(@Valid @RequestBody DeleteIdRequest id) {
    tagService.deleteTag(id);
    return ResponseEntity.status(HttpStatus.OK).build();
  }


  /**
   * 태그 - 작품 연동 페이지 조회
   * @param pageable
   * @param productNo
   * @return
   */
  @GetMapping("/product/{productNo}")
  public ResponseEntity<PageResponse<TagProductResponse>> tagProductPage(Pageable pageable,
      @PathVariable Long productNo) {
    Page<TagProductResponse> response = tagService.retrieveAllTagWithBoolean(pageable, productNo);
    PageResponse<TagProductResponse> result = new PageResponse<>(response);
    return ResponseEntity.status(HttpStatus.OK)
        .body(result);
  }

  /**
   * 태그 - 작품 연동
   * @param request
   * @return
   */
  @PostMapping("/product")
  public ResponseEntity<Void> tagProductConnect(
      @Valid @RequestBody CreateDeleteTagProductRequest request) {
    tagService.createTagProduct(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 태그 - 작품 연동 취소
   * @param request
   * @return
   */
  @DeleteMapping("/product")
  public ResponseEntity<Void> tagProductDisconnect(
      @Valid @RequestBody CreateDeleteTagProductRequest request) {
    tagService.deleteTagProduct(request);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  @GetMapping("/exist/{name}")
  public ResponseEntity<Boolean> retrieveAllTagList(@PathVariable String name) {
    boolean result = tagService.tagNameChecker(name);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }
}
