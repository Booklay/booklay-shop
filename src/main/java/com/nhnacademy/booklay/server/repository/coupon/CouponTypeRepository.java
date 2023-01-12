package com.nhnacademy.booklay.server.repository.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTypeRetrieveResponse;
import com.nhnacademy.booklay.server.entity.CouponType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponTypeRepository extends JpaRepository<CouponType, Long> {
    Page<CouponTypeRetrieveResponse> findAllBy(Pageable pageable);
}
