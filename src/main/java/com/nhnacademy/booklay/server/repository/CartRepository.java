package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Cart.Pk> {
}
