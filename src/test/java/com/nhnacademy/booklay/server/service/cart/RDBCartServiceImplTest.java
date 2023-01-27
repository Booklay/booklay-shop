package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.*;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RDBCartServiceImplTest {

    @InjectMocks
    RDBCartServiceImpl rdbCartService;

    @Mock
    CartService cartService;

    CartAddRequest cartAddRequest;
    CartRetrieveResponse cartRetrieveResponse;
    Cart cart;
    CartDto cartDto;

    @BeforeEach
    void setUp() {
        cartAddRequest = Dummy.getDummyMemberCartAddRequest();
        Product product = Product.builder().price(100L).title("dummy").build();
        ReflectionTestUtils.setField(product, "id", cartAddRequest.getProductNo());
        cart = Cart.builder()
                .pk(new Cart.Pk(Long.parseLong(cartAddRequest.getCartId()), cartAddRequest.getProductNo()))
                .member(Dummy.getDummyMember())
                .product(product)
                .count(cartAddRequest.getCount())
                .build();
        cartDto = new CartDto(cartAddRequest.getProductNo(), cartAddRequest.getCount());
        cartRetrieveResponse = new CartRetrieveResponse(cartAddRequest.getProductNo(),
                product.getTitle(), product.getPrice(), cartAddRequest.getCount());

    }


    @Test
    void getAllCartItems() {
        //given
        given(cartService.retrieveAllCartsByMemberNo(Long.parseLong(cartAddRequest.getCartId()))).willReturn(List.of(cart));

        //when
        List<CartRetrieveResponse> result = rdbCartService.getAllCartItems(cartAddRequest.getCartId());

        //then
        then(cartService).should().retrieveAllCartsByMemberNo(anyLong());
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(cartRetrieveResponse);
    }

    @Test
    void setCartItem() {
        //given
        given(cartService.saveCart(any())).willReturn(cart);

        //when
        rdbCartService.setCartItem(cartAddRequest);

        //then
        then(cartService).should().saveCart(any());
    }

    @Test
    void deleteCartItem() {
        //given

        //when
        rdbCartService.deleteCartItem(cartAddRequest.getCartId(), cartAddRequest.getProductNo());

        //then
        then(cartService).should().deleteCartByMemberNoAndProductNo(any(), any());
    }

    @Test
    void deleteAllCartItems() {
        //given

        //when
        rdbCartService.deleteAllCartItems(cartAddRequest.getCartId());

        //then
        then(cartService).should().deleteAllCartsByMemberNo(any());
    }

    @Test
    void deleteCartItems() {
        //given

        //when
        rdbCartService.deleteCartItems(cartAddRequest.getCartId(), List.of(cartAddRequest.getProductNo()));

        //then
        then(cartService).should().deleteAllCartByPkList(any());
    }
}
