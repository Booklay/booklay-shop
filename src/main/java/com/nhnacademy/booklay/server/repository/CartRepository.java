package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.entity.Cart;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Cart.Pk> {
    List<CartDto> findAllByMember_MemberNo(Long memberNo);

    void deleteCartByPk_MemberId(Long memberNo);
}
