package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import java.util.List;

public interface MemberCartService {
    List<CartRetrieveResponse> getAllCartItems(String key);

    void setCartItem(CartAddRequest cartAddRequest);

    void deleteCartItem(String key, Long productNo);
    void deleteAllCartItems(String key);
    void deleteCartItems(String key, List<Long> productNoList);
}
