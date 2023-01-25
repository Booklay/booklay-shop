package com.nhnacademy.booklay.server.service.cart;

import com.nhnacademy.booklay.server.entity.Cart;
import com.nhnacademy.booklay.server.repository.CartRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    @Override
    public Cart retrieveCartByMemberNoAndProductNo(Long memberNo, Long productNo) {
        return cartRepository.getReferenceById(new Cart.Pk(memberNo, productNo));
    }

    @Override
    public List<Cart> retrieveAllCartsByMemberNo(Long memberNo) {
        return cartRepository.findAllByMember_MemberNo(memberNo);
    }

    @Override
    public Cart saveCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void deleteCartByCart(Cart cart) {
        cartRepository.delete(cart);
    }

    @Override
    public void deleteAllCartByPkList(List<Cart.Pk> pkList) {
        cartRepository.deleteAllById(pkList);
    }

    @Override
    public void deleteAllCartsByMemberNo(Long memberNo) {
        cartRepository.deleteCartByPk_MemberId(memberNo);
    }

    @Override
    public void deleteAllCartsByProductNo(Long productNo) {
        cartRepository.deleteCartByPk_ProductId(productNo);
    }

    @Override
    public void deleteCartByMemberNoAndProductNo(Long memberNo, Long productNo) {
        cartRepository.deleteById(new Cart.Pk(memberNo, productNo));
    }
}
