package com.nhnacademy.booklay.server.controller.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateWishlistRequest;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/product")
public class ProductController {

  private final WishlistService wishlistService;
  private final ProductService productService;

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
