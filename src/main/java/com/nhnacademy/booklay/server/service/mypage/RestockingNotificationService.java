package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;

public interface RestockingNotificationService {

  void createWishlist(CreateDeleteWishlistAndAlarmRequest request);

  void deleteWishlist(CreateDeleteWishlistAndAlarmRequest request);
}
