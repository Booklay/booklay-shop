package com.nhnacademy.booklay.server.repository.order;

import com.nhnacademy.booklay.server.entity.OrderStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusCodeRepository extends JpaRepository<OrderStatusCode, Long> {
}
