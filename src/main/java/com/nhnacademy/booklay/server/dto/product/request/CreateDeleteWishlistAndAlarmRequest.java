package com.nhnacademy.booklay.server.dto.product.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeleteWishlistAndAlarmRequest {

  private Long memberNo;
  private Long productId;

}
