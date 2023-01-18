package com.nhnacademy.booklay.server.repository.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateRetrieveResponse;
import com.nhnacademy.booklay.server.entity.CouponTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long> {
    Page<CouponTemplateRetrieveResponse> findAllBy(Pageable pageable);
}
