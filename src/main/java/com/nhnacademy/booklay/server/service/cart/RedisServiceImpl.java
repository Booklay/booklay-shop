package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisCartService {
    private final RedisTemplate<String, CartDto> redisTemplate;
    private final ProductService productService;
    @Override
    @Transactional(readOnly = true)
    public List<CartRetrieveResponse> getAllCartItems(String key) {
        List<CartDto> cartDtoList = redisTemplate.<String, CartDto>opsForHash().values(key);
        List<Long> productNoList = cartDtoList.stream().map(CartDto::getProductNo)
            .filter(productNo -> productNo != -1L).collect(Collectors.toList());
        List<Product> productList = productService.retrieveProductListByProductNoList(productNoList);
        return getCartRetrieveResponseListFromProductListAndCartDto(productList, cartDtoList);
    }

    @Override
    public void setCartItem(String key, CartDto cartDto) {
        redisTemplate.opsForHash().put(key, cartDto.getProductNo(), cartDto);
        redisTemplate.expire(key, 2, TimeUnit.DAYS);
    }
    @Override
    public void deleteCartItem(String key, Long productNo) {
        redisTemplate.opsForHash().delete(key, productNo);
    }

    @Override
    public void deleteAllCartItems(String key) {
        redisTemplate.opsForHash().delete(key);
    }

    @Override
    public void deleteCartItems(String key, List<Long> productNoList) {
        redisTemplate.opsForHash().delete(key, productNoList);
    }

    @Override
    public Integer checkUUID(String uuid) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(uuid))){
            redisTemplate.opsForHash().put(uuid, "used", new CartDto(-1L, 0));
            redisTemplate.expire(uuid, 2, TimeUnit.DAYS);
            return 1;
        }else {
            return 0;
        }
    }

    private List<CartRetrieveResponse> getCartRetrieveResponseListFromProductListAndCartDto(
        List<Product> productList, List<CartDto> cartDtoList) {
        List<CartRetrieveResponse> cartRetrieveResponseList = new ArrayList<>();
        for (CartDto cartDto : cartDtoList){
            Product product = productList.stream().filter(product1 -> Objects.equals(
                product1.getId(), cartDto.getProductNo())).findFirst().orElse(Product.builder().price(0L).title("상품 없음").build());
            cartRetrieveResponseList.add(new CartRetrieveResponse(product.getId(), product.getTitle(), product.getPrice(), cartDto.getCount()));
        }
        return cartRetrieveResponseList;
    }
}
