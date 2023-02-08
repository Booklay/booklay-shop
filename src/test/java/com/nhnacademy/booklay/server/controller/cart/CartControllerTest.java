package com.nhnacademy.booklay.server.controller.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Cart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.service.cart.CartService;
import com.nhnacademy.booklay.server.service.cart.RDBCartService;
import com.nhnacademy.booklay.server.service.cart.RedisCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CartControllerTest {

    @MockBean
    CartService cartService;
    @MockBean
    RDBCartService rdbCartService;

    @MockBean
    RedisCartService redisService;


    @Autowired
    CartController cartController;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    CartAddRequest memberCartAddRequest;
    CartAddRequest guestCartAddRequest;
    CartRetrieveResponse cartRetrieveResponse;
    Cart cart;
    CartDto cartDto;
    private static final String STRING_CART_ID= "cartId";
    private static final String STRING_PRODUCT_NO = "productNo";
    private static final String STRING_PRODUCT_NO_LIST = "productNoList";
    private static final String URI_PREFIX = "/cart/";
    @BeforeEach
    void setUp() {
        memberCartAddRequest = Dummy.getDummyMemberCartAddRequest();
        guestCartAddRequest = Dummy.getDummyGuestCartAddRequest();
        Product product = Product.builder().price(100L).title("dummy").build();
        ReflectionTestUtils.setField(product, "id", memberCartAddRequest.getProductNo());
        cart = Cart.builder()
                .pk(new Cart.Pk(Long.parseLong(memberCartAddRequest.getCartId()), memberCartAddRequest.getProductNo()))
                .member(Dummy.getDummyMember())
                .product(product)
                .count(memberCartAddRequest.getCount())
                .build();
        cartDto = new CartDto(memberCartAddRequest.getProductNo(), memberCartAddRequest.getCount());
        cartRetrieveResponse = new CartRetrieveResponse(memberCartAddRequest.getProductNo(),
                product.getTitle(), product.getPrice(), memberCartAddRequest.getCount(), List.of(1L), 1L);
    }

    @Test
    void getCart() throws Exception {
        //given
        given(redisService.getAllCartItems(any())).willReturn(List.of(cartRetrieveResponse));

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX)
                        .queryParam(STRING_CART_ID,guestCartAddRequest.getCartId())
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*].['productNo']").value(guestCartAddRequest.getProductNo().intValue()));
    }

    @Test
    void setCart() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(post(URI_PREFIX)
                    .content(objectMapper.writeValueAsString(guestCartAddRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());
        then(redisService).should(times(1)).setCartItem(any());
    }

    @Test
    void deleteCart() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete(URI_PREFIX)
                        .queryParam(STRING_CART_ID, guestCartAddRequest.getCartId())
                        .queryParam(STRING_PRODUCT_NO, guestCartAddRequest.getProductNo().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk());
        then(redisService).should(times(1)).deleteCartItem(any(), any());
    }

    @Test
    void deleteAllCart() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete(URI_PREFIX+"all")
                .queryParam(STRING_CART_ID, guestCartAddRequest.getCartId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());
        then(redisService).should(times(1)).deleteAllCartItems(any());
    }

    @Test
    void deleteCarts() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete(URI_PREFIX+"buy")
                .queryParam(STRING_CART_ID, guestCartAddRequest.getCartId())
                .queryParam(STRING_PRODUCT_NO_LIST, guestCartAddRequest.getProductNo().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());
        then(redisService).should(times(1)).deleteCartItems(any(), any());
    }

    @Test
    void moveRedisToRDB() throws Exception {
        //given
        given(redisService.getAllCartItems(any())).willReturn(List.of(cartRetrieveResponse));
        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX+"login/"+guestCartAddRequest.getCartId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());
        then(redisService).should(times(1)).deleteAllCartItems(any());
        then(redisService).should(times(1)).getAllCartItems(any());
    }

    @Test
    void uuidCheck() throws Exception {
        given(redisService.checkUUID(any())).willReturn(1);
        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX+"uuid/"+guestCartAddRequest.getCartId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());
        then(redisService).should(times(1)).checkUUID(any());
    }
}
