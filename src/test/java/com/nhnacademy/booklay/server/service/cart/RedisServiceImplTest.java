//package com.nhnacademy.booklay.server.service.cart;
//
//import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
//import com.nhnacademy.booklay.server.dto.cart.CartDto;
//import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
//import com.nhnacademy.booklay.server.dummy.Dummy;
//import com.nhnacademy.booklay.server.entity.Cart;
//import com.nhnacademy.booklay.server.entity.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//
//@ExtendWith(MockitoExtension.class)
//class RedisServiceImplTest {
//
//    @InjectMocks
//    RedisServiceImpl redisService;
//    @Mock
//    RedisTemplate redisTemplate;
//
//    CartAddRequest cartAddRequest;
//    CartRetrieveResponse cartRetrieveResponse;
//    Cart cart;
//    CartDto cartDto;
//
//    @BeforeEach
//    void setUp() {
//        cartAddRequest = Dummy.getDummyGuestCartAddRequest();
//        Product product = Product.builder().price(100L).title("dummy").build();
//        ReflectionTestUtils.setField(product, "id", cartAddRequest.getProductNo());
//        cartDto = new CartDto(cartAddRequest.getProductNo(), cartAddRequest.getCount());
//        cartRetrieveResponse = new CartRetrieveResponse(cartAddRequest.getProductNo(),
//                product.getTitle(), product.getPrice(), cartAddRequest.getCount());
//
//    }
//
//    @Test
//    void getAllCartItems() {
//        //given
////        given(redisTemplate.opsForHash()).willReturn(new DefaultHashOperations(this));
////        given(redisTemplate.opsForHash().values(cartAddRequest.getCartId())).willReturn(List.of(cartDto));
//        given(redisTemplate.opsForHash().values(any())).willReturn(List.of());
//
//        //when
//        List<CartRetrieveResponse> result = redisService.getAllCartItems(cartAddRequest.getCartId());
//
//        //then
//        then(redisTemplate).should().opsForHash().values(any());
//        assertThat(result.size()).isEqualTo(1);
//        assertThat(result.get(0)).usingRecursiveComparison().isEqualTo(cartRetrieveResponse);
//    }
//
//    @Test
//    void setCartItem() {
//    }
//
//    @Test
//    void deleteCartItem() {
//    }
//
//    @Test
//    void deleteAllCartItems() {
//    }
//
//    @Test
//    void deleteCartItems() {
//    }
//}
