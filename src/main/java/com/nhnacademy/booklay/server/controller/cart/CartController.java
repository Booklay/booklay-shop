package com.nhnacademy.booklay.server.controller.cart;


import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.cart.CartServiceAndKeyDto;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.service.cart.CartService;
import com.nhnacademy.booklay.server.service.cart.RDBCartService;
import com.nhnacademy.booklay.server.service.cart.RedisCartService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {

    private final RDBCartService rdbCartService;
    private final RedisCartService redisCartService;
    private static final String STRING_CART_ID= "CART_ID";
    private static final String STRING_PRODUCT_NO = "productNo";
    private static final String STRING_PRODUCT_NO_LIST = "productNoList";
    //todo @ModelAttribute Member 추가해줄것
    @GetMapping
    public ResponseEntity<List<CartDto>> getCart(@ModelAttribute("member")Member member,
                                                 @RequestParam(STRING_CART_ID) String cartId){
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(member, cartId);
        List<CartDto> cartList = cartServiceAndKeyDto.getCartService().getAllCartItems(
            cartServiceAndKeyDto.getKey());
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(cartList);
    }

    @PostMapping
    public ResponseEntity<Void> setCart(@ModelAttribute("member")Member member,
                                        @RequestParam(STRING_CART_ID) String cartId,
                                        @RequestBody CartDto cartDto){
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(member, cartId);
        cartServiceAndKeyDto.getCartService().setCartItem(cartServiceAndKeyDto.getKey(), cartDto);
        return ResponseEntity.ok()
            .build();
    }

    //fixme mapping 방식 고려 필요
    @PutMapping
    public ResponseEntity<Void> deleteCart(@ModelAttribute("member")Member member,
                                           @RequestParam(STRING_CART_ID) String cartId,
                                           @RequestParam(STRING_PRODUCT_NO) Long productNo){
        CartServiceAndKeyDto cartServiceAndKeyDto = getCartServiceAndKeyDto(member, cartId);
        cartServiceAndKeyDto.getCartService().deleteCartItem(cartServiceAndKeyDto.getKey(),
            productNo);
        return ResponseEntity.ok()
            .build();
    }

    @PutMapping("all")
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

    @GetMapping("uuid/{uuid}")
    public ResponseEntity<Integer> uuidCheck(@PathVariable String uuid){
        return ResponseEntity.ok(redisCartService.checkUUID(uuid));
    }

    private CartServiceAndKeyDto getCartServiceAndKeyDto(Member member, String cartId){
        CartService cartService;
        String key;
        if(Objects.isNull(member)){
            cartService = redisCartService;
            key = cartId;
        }else {
            cartService = rdbCartService;
            key = member.getMemberNo().toString();
        }
        return new CartServiceAndKeyDto(cartService, key);
    }
}
