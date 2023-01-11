package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.product.request.CreateWishlistRequest;

public interface WishlistService {

  void createWishlist(CreateWishlistRequest request);

  void deleteWishlist(CreateWishlistRequest request);
}
