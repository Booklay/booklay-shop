package com.nhnacademy.booklay.server.dto.mypage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class WishlistAndAlarmBooleanResponse {

  Boolean wishlist;
  Boolean alarm;
}
