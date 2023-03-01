package com.nhnacademy.booklay.server.controller.cart;


import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.cart.CartServiceAndKeyDto;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.service.cart.MemberCartService;
import com.nhnacademy.booklay.server.service.cart.RDBCartService;
import com.nhnacademy.booklay.server.service.cart.RedisCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {

    private final RDBCartService rdbCartService;
    private final RedisCartService redisCartService;
    private static final String STRING_CART_ID = "cartId";
    private static final String STRING_PRODUCT_NO = "productNo";
    private static final String STRING_PRODUCT_NO_LIST = "productNoList";

    @GetMapping
    public ResponseEntity<List<CartRetrieveResponse>> getCart(
        MemberInfo memberInfo,
        @RequestParam(STRING_CART_ID) String cartId) {
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(memberInfo.getMemberNo(), cartId);
        List<CartRetrieveResponse> cartList = cartServiceAndKeyDto.getCartService().getAllCartItems(
            cartServiceAndKeyDto.getKey());
        return ResponseEntity.status(HttpStatus.OK)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(cartList);
    }

    @PostMapping
    public ResponseEntity<Void> setCart(MemberInfo memberInfo,
                                        @Valid @RequestBody CartAddRequest cartAddRequest) {
        CartServiceAndKeyDto cartServiceAndKeyDto =
            getCartServiceAndKeyDto(memberInfo.getMemberNo(), cartAddRequest.getCartId());
        cartAddRequest.setCartId(cartServiceAndKeyDto.getKey());
        cartServiceAndKeyDto.getCartService().setCartItem(cartAddRequest);
        return ResponseEntity.ok()
                             .build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCart(MemberInfo memberInfo,
                                           @RequestParam(value = STRING_CART_ID, required = false) String cartId,
                                           @RequestParam(value = STRING_PRODUCT_NO, required = false) Long productNo) {
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(memberInfo.getMemberNo(), cartId);
        cartServiceAndKeyDto.getCartService().deleteCartItem(cartServiceAndKeyDto.getKey(),
                                                             productNo);
        return ResponseEntity.ok()
                             .build();
    }

    @DeleteMapping("all")
    public ResponseEntity<Void> deleteAllCart(MemberInfo memberInfo,
                                              @RequestParam(STRING_CART_ID) String cartId) {
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(memberInfo.getMemberNo(), cartId);
        cartServiceAndKeyDto.getCartService().deleteAllCartItems(cartServiceAndKeyDto.getKey());
        return ResponseEntity.ok()
                             .build();
    }

    @DeleteMapping("/buy")
    public ResponseEntity<Void> deleteCarts(MemberInfo memberInfo,
                                            @RequestParam(value = STRING_CART_ID,required = false) String cartId,
                                            @RequestParam(value = STRING_PRODUCT_NO_LIST,required = false)
                                            List<Long> productNoList) {
        if (productNoList != null && !(memberInfo.getMemberNo() == null && cartId == null)){
            CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(memberInfo.getMemberNo(), cartId);
            cartServiceAndKeyDto.getCartService().deleteCartItems(cartServiceAndKeyDto.getKey(),
                    productNoList);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("move/{cartId}")
    public ResponseEntity<Void> moveRedisToRDB(MemberInfo memberInfo,
                                               @PathVariable String cartId) {
        List<CartRetrieveResponse> allCartItems = redisCartService.getAllCartItems(cartId);
        redisCartService.deleteAllCartItems(cartId);
        for (CartRetrieveResponse cartRetrieveResponse : allCartItems) {
            rdbCartService.setCartItem(
                new CartAddRequest(memberInfo.getMemberNo().toString(), cartRetrieveResponse.getProductNo(),
                                   cartRetrieveResponse.getProductCount()));
        }
        return ResponseEntity.ok()
                             .build();
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Integer> uuidCheck(@PathVariable String uuid) {
        return ResponseEntity.ok(redisCartService.checkUUID(uuid));
    }

    private CartServiceAndKeyDto getCartServiceAndKeyDto(Long memberNo, String cartId) {
        MemberCartService cartService;
        String key;
        if (Objects.isNull(memberNo)) {
            cartService = redisCartService;
            key = cartId;
        } else {
            cartService = rdbCartService;
            key = memberNo.toString();
        }
        return new CartServiceAndKeyDto(cartService, key);
    }
}
