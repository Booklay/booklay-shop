package com.nhnacademy.booklay.server.service.coupon;



import com.nhnacademy.booklay.server.dto.coupon.CouponIssueRequest;
import com.nhnacademy.booklay.server.entity.CouponTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author 오준후
 */
@Service
@RequiredArgsConstructor
public class CouponComplexServiceImpl implements CouponComplexService {

    private final CouponAdminService couponAdminService;

    @Override
    public void createAndIssueCouponByTemplate(CouponTemplate couponTemplate, Long memberId){
        //todo couponAdminService.createCoupon 이 반환해주는 couponId로 변경
        couponAdminService.createCoupon(couponTemplate.toCouponCreateRequest());
        couponAdminService.issueCoupon(new CouponIssueRequest(0L, memberId));
    }
}
