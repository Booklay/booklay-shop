package com.nhnacademy.booklay.server.controller.mypage;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.mypage.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.service.mypage.RestockingNotificationService;
import com.nhnacademy.booklay.server.service.mypage.WishlistService;
import java.io.IOException;
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
   * @param pageable
   * @param memberId
   * @return
   * @throws IOException
   */
  @GetMapping("/wishlist/{memberId}")
  public PageResponse<RetrieveProductResponse> retrieveWishlist(Pageable pageable,
      @PathVariable Long memberId)
      throws IOException {
    Page<RetrieveProductResponse> responses = wishlistService.retrievePage(memberId, pageable);
    return new PageResponse<>(responses);
  }


  /**
   * 위시리스트 등록 삭제
   * @param request
   */
  @PostMapping("/wishlist")
  public void createWishlist(@RequestBody CreateDeleteWishlistAndAlarmRequest request) {
    wishlistService.createWishlist(request);
  }


  /**
   * 재입고 알림 목록 조회
   * @param memberId
   * @param pageable
   * @return
   * @throws IOException
   */
  @GetMapping("/alarm/{memberId}")
  public PageResponse<RetrieveProductResponse> retrieveNotification(
      Pageable pageable, @PathVariable Long memberId)
      throws IOException {
    Page<RetrieveProductResponse> responses = restockingNotificationService.retrievePage(memberId,
        pageable);
    return new PageResponse<>(responses);
  }

  /**
   * 위시리스트 삭제
   * @param request
   */
  @DeleteMapping("/wishlist")
  public void deleteWishlist(@RequestBody CreateDeleteWishlistAndAlarmRequest request) {
    wishlistService.deleteWishlist(request);
  }

  /**
   * 재입고 알림 등록
   * @param request
   */
  @PostMapping("/alarm")
  public void createAlarm(@RequestBody CreateDeleteWishlistAndAlarmRequest request) {
    restockingNotificationService.createAlarm(request);
  }

  /**
   * 재입고 알림 등록 취소
   * @param request
   */
  @DeleteMapping("/alarm")
  public void deleteAlarm(@RequestBody CreateDeleteWishlistAndAlarmRequest request) {
    restockingNotificationService.deleteAlarm(request);
  }

  @GetMapping("/boolean/{memberNo}")
  public ResponseEntity<WishlistAndAlarmBooleanResponse> retrieveMemberProduct(
      @PathVariable Long memberNo) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(wishlistService.retrieveExists(memberNo));
  }
}
