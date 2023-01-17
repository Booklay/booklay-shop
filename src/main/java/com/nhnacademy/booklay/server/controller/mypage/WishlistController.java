package com.nhnacademy.booklay.server.controller.mypage;

import com.nhnacademy.booklay.server.dto.product.request.CreateWishlistRequest;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 최규태
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class WishlistController {

  private final WishlistService wishlistService;

  //위시리스트 등록 삭제
  @PostMapping("/wishlist")
  public void createWishlist(CreateWishlistRequest request) {
    wishlistService.createWishlist(request);
  }

  @DeleteMapping("/wishlist")
  public void deleteWishlist(CreateWishlistRequest request) {
    wishlistService.deleteWishlist(request);
  }
}
