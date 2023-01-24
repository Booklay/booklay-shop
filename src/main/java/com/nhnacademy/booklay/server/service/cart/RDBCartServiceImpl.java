package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.entity.Cart;
import com.nhnacademy.booklay.server.repository.CartRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RDBCartServiceImpl implements RDBCartService {
    CartRepository cartRepository;
    @Override
    @Transactional(readOnly = true)
    public List<CartDto> getAllCartItems(String key) {
        return cartRepository.findAllByMember_MemberNo(Long.parseLong(key));
    }

    @Override
    public void setCartItem(String key, CartDto cartDto) {
        Cart cart = Cart.builder()
            .pk(new Cart.Pk(Long.parseLong(key), cartDto.getProductNo()))
            .count(cartDto.getCount())
            .build();
        cartRepository.save(cart);
    }

    @Override
    public void deleteCartItem(String key, Long productNo) {
        cartRepository.deleteById(new Cart.Pk(Long.parseLong(key), productNo));
    }

    @Override
    public void deleteAllCartItems(String key) {
        cartRepository.deleteCartByPk_MemberId(Long.parseLong(key));
    }

    @Override
    public void deleteCartItems(String key, List<Long> productNoList) {
        List<Cart.Pk> pkList = productNoList.stream().map(cartDto -> new Cart.Pk(Long.parseLong(key), cartDto)).collect(
            Collectors.toList());
        cartRepository.deleteAllById(pkList);
    }
}
