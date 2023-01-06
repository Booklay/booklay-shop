package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
