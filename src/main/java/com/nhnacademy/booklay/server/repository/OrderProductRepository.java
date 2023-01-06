package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
}
