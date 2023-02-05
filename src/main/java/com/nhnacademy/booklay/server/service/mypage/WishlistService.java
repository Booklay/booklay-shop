package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;

/**
 * @author 최규태
 */

public interface WishlistService {

    void createWishlist(CreateDeleteWishlistAndAlarmRequest request);

    void deleteWishlist(CreateDeleteWishlistAndAlarmRequest request);
}
