package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Cart;
import com.nhnacademy.booklay.server.service.category.CategoryProductService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RDBCartServiceImpl implements RDBCartService {
    private final CartService cartService;
    private final CategoryProductService categoryProductService;

    @Override
    @Transactional(readOnly = true)
    public List<CartRetrieveResponse> getAllCartItems(String key) {
        List<Cart> cartList = cartService.retrieveAllCartsByMemberNo(Long.parseLong(key));
        return cartList.stream().map(cart -> new CartRetrieveResponse(cart.getProduct().getId(),
                cart.getProduct().getTitle(), cart.getProduct().getPrice(), cart.getCount(),
                categoryProductService.retrieveCategoryIdListByProductId(cart.getProduct().getId())))
            .collect(Collectors.toList());
    }

    @Override
    public void setCartItem(CartAddRequest cartAddRequest) {
        Cart cart = Cart.builder()
                        .pk(new Cart.Pk(Long.parseLong(cartAddRequest.getCartId()),
                                        cartAddRequest.getProductNo()))
                        .count(cartAddRequest.getCount())
                        .build();
        cartService.saveCart(cart);
    }

    @Override
    public void deleteCartItem(String key, Long productNo) {
        cartService.deleteCartByMemberNoAndProductNo(Long.parseLong(key), productNo);
    }

    @Override
    public void deleteAllCartItems(String key) {
        cartService.deleteAllCartsByMemberNo(Long.parseLong(key));
    }

    @Override
    public void deleteCartItems(String key, List<Long> productNoList) {
        List<Cart.Pk> pkList =
            productNoList.stream().map(cartDto -> new Cart.Pk(Long.parseLong(key), cartDto))
                         .collect(
                             Collectors.toList());
        cartService.deleteAllCartByPkList(pkList);
    }


}
