package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import com.nhnacademy.booklay.server.repository.ProductRepository;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import com.nhnacademy.booklay.server.repository.coupon.CouponTypeRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponAdminServiceImpl implements CouponAdminService{

    private final CouponRepository couponRepository;
    private final CouponTypeRepository couponTypeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public void createCoupon(CouponCURequest couponRequest) {
        CouponType couponType = couponTypeRepository.findById(couponRequest.getTypeCode()).orElseThrow(() -> new IllegalArgumentException("No Such Coupon Type."));

        Coupon coupon = Coupon.builder()
            .couponType(couponType)
            .name(couponRequest.getName())
            .amount(couponRequest.getAmount())
            .minimumUseAmount(couponRequest.getMinimumUseAmount())
            .maximumDiscountAmount(couponRequest.getMaximumDiscountAmount())
            .issuanceDeadlineAt(couponRequest.getIssuanceDeadlineAt())
            .isDuplicatable(couponRequest.getIsDuplicatable())
            .build();

        couponRepository.save(coupon);
    }

    @Transactional(readOnly = true)
    public List<CouponRetrieveResponse> retrieveAllCoupons() {
        List<Coupon> couponList = couponRepository.findAll();

        return couponList.stream().map(c -> CouponRetrieveResponse.fromEntity(c)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CouponDetailRetrieveResponse retrieveCoupon(Long couponId) {
        return CouponDetailRetrieveResponse.fromEntity(couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("No Such Coupon.")));
    }

    public void updateCoupon(Long couponId, CouponCURequest couponRequest) {

    }

    public void deleteCoupon(Long couponId) {
        couponRepository.deleteById(couponId);
    }

}
