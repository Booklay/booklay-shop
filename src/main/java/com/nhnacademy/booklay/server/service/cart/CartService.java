package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.entity.Cart;
import java.util.List;

public interface CartService {
    Cart retrieveCartByMemberNoAndProductNo(Long memberNo, Long productNo);
    List<Cart> retrieveAllCartsByMemberNo(Long memberNo);
    Cart saveCart(Cart cart);
    void deleteCartByCart(Cart cart);
    void deleteAllCartByPkList(List<Cart.Pk> pkList);
    void deleteAllCartsByMemberNo(Long memberNo);
    void deleteAllCartsByProductNo(Long productNo);
    void deleteCartByMemberNoAndProductNo(Long memberNo, Long productNo);
}
