package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
