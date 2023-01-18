package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateWishlistRequest;

/**
 * @author 최규태
 */

public interface WishlistService {

  void createWishlist(CreateWishlistRequest request);

  void deleteWishlist(CreateWishlistRequest request);
}
