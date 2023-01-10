package com.nhnacademy.booklay.server.dto.product.request;

import lombok.Getter;

@Getter
public class CreateWishlistRequest {

  private Long memberId;
  private Long productId;

  public CreateWishlistRequest(Long memberId, Long productId) {
    this.memberId = memberId;
    this.productId = productId;
  }
}
