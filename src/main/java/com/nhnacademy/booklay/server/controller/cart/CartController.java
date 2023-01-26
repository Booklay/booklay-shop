package com.nhnacademy.booklay.server.controller.cart;


import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.cart.CartServiceAndKeyDto;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.service.cart.MemberCartService;
import com.nhnacademy.booklay.server.service.cart.RDBCartService;
import com.nhnacademy.booklay.server.service.cart.RedisCartService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {

    private final RDBCartService rdbCartService;
    private final RedisCartService redisCartService;
    private static final String STRING_CART_ID= "cartId";
    private static final String STRING_PRODUCT_NO = "productNo";
    private static final String STRING_PRODUCT_NO_LIST = "productNoList";
    //todo @ModelAttribute Member 추가해줄것
    @GetMapping
    public ResponseEntity<List<CartRetrieveResponse>> getCart(@ModelAttribute("member")Member member,
                                                 @RequestParam(STRING_CART_ID) String cartId){
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(member, cartId);
        List<CartRetrieveResponse> cartList = cartServiceAndKeyDto.getCartService().getAllCartItems(
            cartServiceAndKeyDto.getKey());
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(cartList);
    }

    @PostMapping
    public ResponseEntity<Void> setCart(@ModelAttribute("member")Member member,
                                        @RequestBody CartAddRequest cartAddRequest){
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(member, cartAddRequest.getCartId());
        cartServiceAndKeyDto.getCartService().setCartItem(cartAddRequest);
        return ResponseEntity.ok()
            .build();
    }

    //fixme mapping 방식 고려 필요
    @DeleteMapping
    public ResponseEntity<Void> deleteCart(@ModelAttribute("member")Member member,
                                           @RequestParam(STRING_CART_ID) String cartId,
                                           @RequestParam(STRING_PRODUCT_NO) Long productNo){
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(member, cartId);
        cartServiceAndKeyDto.getCartService().deleteCartItem(cartServiceAndKeyDto.getKey(),
            productNo);
        return ResponseEntity.ok()
            .build();
    }

    @DeleteMapping("all")
    public ResponseEntity<Void> deleteAllCart(@ModelAttribute("member")Member member,
                                              @RequestParam(STRING_CART_ID) String cartId){
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(member, cartId);
        cartServiceAndKeyDto.getCartService().deleteAllCartItems(cartServiceAndKeyDto.getKey());
        return ResponseEntity.ok()
            .build();
    }

    @PutMapping("buy")
    public ResponseEntity<Void> deleteCarts(@ModelAttribute("member")Member member,
                                            @RequestParam(STRING_CART_ID) String cartId,
                                           @RequestParam(STRING_PRODUCT_NO_LIST) List<Long> productNoList){
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(member, cartId);
        cartServiceAndKeyDto.getCartService().deleteCartItems(cartServiceAndKeyDto.getKey(),
            productNoList);
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("login/{cartId}")
    public ResponseEntity<Void> moveRedisToRDB(@ModelAttribute("member")Member member,
                                               @PathVariable String cartId){
        List<CartRetrieveResponse> allCartItems = redisCartService.getAllCartItems(cartId);
        redisCartService.deleteAllCartItems(cartId);
        for (CartRetrieveResponse cartRetrieveResponse : allCartItems){
            rdbCartService.setCartItem(new CartAddRequest(cartId, cartRetrieveResponse.getProductNo(), cartRetrieveResponse.getProductCount()));
        }
        return ResponseEntity.ok()
            .build();
    }

    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Integer> uuidCheck(@PathVariable String uuid){
        return ResponseEntity.ok(redisCartService.checkUUID(uuid));
    }

    private CartServiceAndKeyDto getCartServiceAndKeyDto(Member member, String cartId){
        MemberCartService cartService;
        String key;
        if(Objects.isNull(member) || member.getMemberNo() == null){
            cartService = redisCartService;
            key = cartId;
        }else {
            cartService = rdbCartService;
            key = member.getMemberNo().toString();
        }
        return new CartServiceAndKeyDto(cartService, key);
    }
}
