package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.dto.cart.CartAddRequest;
import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.service.category.CategoryProductService;
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
public class RedisCartServiceImpl implements RedisCartService {

    private final RedisTemplate<String, CartDto> redisTemplate;
    private final ProductService productService;
    private final CategoryProductService categoryProductService;

    @Override
    @Transactional(readOnly = true)
    public List<CartRetrieveResponse> getAllCartItems(String key) {
        List<CartDto> cartDtoList = redisTemplate.<String, CartDto>opsForHash().values(key);
        List<Long> productNoList = cartDtoList.stream().map(CartDto::getProductNo)
                                              .filter(productNo -> productNo != -1L)
                                              .collect(Collectors.toList());
        List<Product> productList =
            productService.retrieveProductListByProductNoList(productNoList);
        return getCartRetrieveResponseListFromProductListAndCartDto(productList, cartDtoList);
    }

    @Override
    public void setCartItem(CartAddRequest cartAddRequest) {
        redisTemplate.opsForHash()
                     .put(cartAddRequest.getCartId(), cartAddRequest.getProductNo().toString(),
                          new CartDto(cartAddRequest.getProductNo(), cartAddRequest.getCount()));
        redisTemplate.expire(cartAddRequest.getCartId(), 2, TimeUnit.DAYS);
    }

    @Override
    public void deleteCartItem(String key, Long productNo) {
        redisTemplate.opsForHash().delete(key, productNo.toString());
    }

    @Override
    public void deleteAllCartItems(String key) {
        redisTemplate.opsForHash()
                     .delete(key, redisTemplate.<String, CartDto>opsForHash().keys(key).toArray());
    }

    @Override
    public void deleteCartItems(String key, List<Long> productNoList) {
        redisTemplate.opsForHash().delete(key,
                                          productNoList.stream().map(Object::toString)
                                                       .collect(Collectors.toList()));
        createUsedInRedis(key);
    }

    @Override
    public Integer checkUUID(String uuid) {
        if (Boolean.TRUE.equals(redisTemplate.hasKey(uuid))) {
            return 0;
        } else {
            createUsedInRedis(uuid);
            return 1;
        }
    }

    private List<CartRetrieveResponse> getCartRetrieveResponseListFromProductListAndCartDto(
        List<Product> productList, List<CartDto> cartDtoList) {
        List<CartRetrieveResponse> cartRetrieveResponseList = new ArrayList<>();
        for (CartDto cartDto : cartDtoList) {
            if (cartDto.getProductNo() == -1L) {
                continue;
            }
            Product product = productList.stream().filter(product1 -> Objects.equals(
                                             product1.getId(), cartDto.getProductNo())).findFirst()
                                         .orElse(
                                             Product.builder().price(0L).title("상품 없음").build());
            cartRetrieveResponseList.add(
                new CartRetrieveResponse(product.getId(), product.getTitle(), product.getPrice(),
                                         cartDto.getCount(),
                    categoryProductService.retrieveCategoryIdListByProductId(product.getId())
                ,product.getThumbnailNo()));
        }

        cartRetrieveResponseList.sort((o1, o2) -> o1.getProductNo()>o2.getProductNo()?1:-1);
        return cartRetrieveResponseList;
    }

    private void createUsedInRedis(String key) {
        redisTemplate.opsForHash().put(key, "used", new CartDto(-1L, 0));
        redisTemplate.expire(key, 2, TimeUnit.DAYS);
    }
}
