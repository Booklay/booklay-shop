package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Cart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class CartServiceImplTest {
    @InjectMocks
    CartServiceImpl cartService;

    @Mock
    CartRepository cartRepository;
    Cart cart;

    @BeforeEach
    void setUp() {
        CartAddRequest cartAddRequest = Dummy.getDummyMemberCartAddRequest();
        Product product = Product.builder().price(100L).title("dummy").build();
        ReflectionTestUtils.setField(product, "id", cartAddRequest.getProductNo());
        cart = Cart.builder()
                .pk(new Cart.Pk(Long.parseLong(cartAddRequest.getCartId()), cartAddRequest.getProductNo()))
                .member(Dummy.getDummyMember())
                .product(product)
                .count(cartAddRequest.getCount())
                .build();
    }
    @Test
    void retrieveCartByMemberNoAndProductNo() {
        //given
        given(cartRepository.getReferenceById(any())).willReturn(cart);

        //when
        Cart result = cartService.retrieveCartByMemberNoAndProductNo(cart.getMember().getMemberNo(), cart.getProduct().getId());

        //then
        assertAll(
                () -> assertThat(result).usingRecursiveComparison().isEqualTo(cart)
        );
    }

    @Test
    void retrieveAllCartsByMemberNo() {
        //given
        given(cartRepository.findAllByMember_MemberNo(any())).willReturn(List.of(cart));

        //when
        List<Cart> result = cartService.retrieveAllCartsByMemberNo(cart.getMember().getMemberNo());

        //then
        assertAll(
                () -> assertThat(result).usingRecursiveComparison().isEqualTo(List.of(cart))
        );
    }

    @Test
    void saveCart() {
        //given
        given(cartRepository.save(any())).willReturn(cart);

        //when
        Cart result = cartService.saveCart(cart);


        //then
        assertAll(
                () -> assertThat(result).usingRecursiveComparison().isEqualTo(cart)
        );
    }

    @Test
    void deleteCartByCart() {
        //when
        cartService.deleteCartByCart(cart);

        //then
        then(cartRepository).should().delete(any());
    }

    @Test
    void deleteAllCartByPkList() {
        //when
        cartService.deleteAllCartByPkList(List.of(cart.getPk()));

        //then
        then(cartRepository).should().deleteAllById(any());
    }

    @Test
    void deleteAllCartsByMemberNo() {
        //when
        cartService.deleteAllCartsByMemberNo(cart.getMember().getMemberNo());

        //then
        then(cartRepository).should().deleteCartByPk_MemberId(any());
    }

    @Test
    void deleteAllCartsByProductNo() {
        //when
        cartService.deleteAllCartsByProductNo(cart.getProduct().getId());

        //then
        then(cartRepository).should().deleteCartByPk_ProductId(any());
    }

    @Test
    void deleteCartByMemberNoAndProductNo() {
        //when
        cartService.deleteCartByMemberNoAndProductNo(cart.getMember().getMemberNo(), cart.getProduct().getId());

        //then
        then(cartRepository).should().deleteById(any());
    }
}
