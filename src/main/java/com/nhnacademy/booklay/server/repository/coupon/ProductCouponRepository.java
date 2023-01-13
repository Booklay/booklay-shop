package com.nhnacademy.booklay.server.repository.coupon;

import com.nhnacademy.booklay.server.entity.ProductCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCouponRepository extends JpaRepository<ProductCoupon, Long> {
}
