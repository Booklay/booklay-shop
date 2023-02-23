package com.nhnacademy.booklay.server.dto.mypage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishlistAndAlarmBooleanResponse {

  Boolean wishlist;
  Boolean alarm;
}
