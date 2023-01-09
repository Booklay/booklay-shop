package com.nhnacademy.booklay.server.repository.coupon;

import com.nhnacademy.booklay.server.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Page<Coupon> findAllBy(Pageable pageable);
}
