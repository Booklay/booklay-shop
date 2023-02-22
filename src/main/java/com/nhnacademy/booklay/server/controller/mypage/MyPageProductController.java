package com.nhnacademy.booklay.server.controller.mypage;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.mypage.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.server.dto.product.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.mypage.RestockingNotificationService;
import com.nhnacademy.booklay.server.service.mypage.WishlistService;
import java.io.IOException;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 최규태
 */

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/mypage/product")
public class MyPageProductController {

  private final WishlistService wishlistService;
  private final RestockingNotificationService restockingNotificationService;

  /**
   * 위시리스트 목록 조회
   *
   * @param pageable
   * @param memberNo
   * @return
   * @throws IOException
   */
  @GetMapping("/wishlist/{memberNo}")
  public ResponseEntity<PageResponse<RetrieveProductResponse>> retrieveWishlist(Pageable pageable,
      @PathVariable Long memberNo)
      throws IOException {
    Page<RetrieveProductResponse> responses = wishlistService.retrievePage(memberNo, pageable);
    PageResponse<RetrieveProductResponse> page = new PageResponse<>(responses);

    return ResponseEntity.status(HttpStatus.OK)
        .body(page);
  }

  /**
   * 마이페이지 인덱스 화면에서 위시리스트 최대 5개 호출
   * @param memberNo
   * @return
   * @throws IOException
   */
  @GetMapping("/index/wishlist/{memberNo}")
  public ResponseEntity<List<RetrieveProductResponse>> retrieveIndexWishlist(
      @PathVariable Long memberNo)
      throws IOException {
    List<RetrieveProductResponse> responses = wishlistService.retrieveWishlist(memberNo);

    return ResponseEntity.status(HttpStatus.OK)
        .body(responses);
  }

  /**
   * 위시리스트 등록
   *
   * @param request
   */
  @PostMapping("/wishlist")
  public ResponseEntity<Void> createWishlist(@RequestBody WishlistAndAlarmRequest request) {
    wishlistService.createWishlist(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .build();
  }

  /**
   * 재입고 알림 목록 조회
   *
   * @param memberNo
   * @param pageable
   * @return
   * @throws IOException
   */
  @GetMapping("/alarm/{memberNo}")
  public ResponseEntity<PageResponse<RetrieveProductResponse>> retrieveNotification(
      Pageable pageable, @PathVariable Long memberNo)
      throws IOException {
    Page<RetrieveProductResponse> responses = restockingNotificationService.retrievePage(memberNo,
        pageable);
    PageResponse<RetrieveProductResponse> page = new PageResponse<>(responses);

    return ResponseEntity.status(HttpStatus.OK)
        .body(page);
  }

  /**
   * 위시리스트 삭제
   *
   * @param request
   */
  @DeleteMapping("/wishlist")
  public ResponseEntity<Void> deleteWishlist(@RequestBody WishlistAndAlarmRequest request) {
    wishlistService.deleteWishlist(request);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }

  /**
   * 재입고 알림 등록
   *
   * @param request
   */
  @PostMapping("/alarm")
  public ResponseEntity<Void> createAlarm(@RequestBody WishlistAndAlarmRequest request) {
    restockingNotificationService.createAlarm(request);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * 재입고 알림 등록 취소
   *
   * @param request
   */
  @DeleteMapping("/alarm")
  public ResponseEntity<Void> deleteAlarm(@RequestBody WishlistAndAlarmRequest request) {
    restockingNotificationService.deleteAlarm(request);
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .build();
  }

  /**
   * 상품 상세 정보 조회시 위시리스트, 재입고 알림 등록 여부 확인
   * @param request
   * @return
   */
  @PostMapping("/boolean")
  public ResponseEntity<WishlistAndAlarmBooleanResponse> retrieveMemberProduct(
      @RequestBody WishlistAndAlarmRequest request) {
    WishlistAndAlarmBooleanResponse body = wishlistService.retrieveExists(request);
    return ResponseEntity.status(HttpStatus.OK)
        .body(body);
  }
}
