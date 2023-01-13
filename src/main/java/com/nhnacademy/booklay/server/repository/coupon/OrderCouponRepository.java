package com.nhnacademy.booklay.server.repository.coupon;

import com.nhnacademy.booklay.server.entity.OrderCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCouponRepository extends JpaRepository<OrderCoupon, Long> {
}
