package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateUpdateRequest;
import com.nhnacademy.booklay.server.entity.CouponTemplate;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.coupon.CouponTemplateRepository;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
/**
 *
 * @author 오준후
 */
@Service
@RequiredArgsConstructor
public class CouponTemplateServiceImpl implements CouponTemplateService{
    private final CouponTemplateRepository couponTemplateRepository;
    private final CouponService couponService;
    private final CouponTypeService couponTypeService;
    @Override
    public CouponTemplate createCouponTemplate(
        @Valid CouponTemplateCreateRequest couponTemplateCreateRequest) {
        //todo imageService 완성시 변경
        CouponTemplate couponTemplate = couponTemplateCreateRequest.toEntity(1L);
        return couponTemplateRepository.save(couponTemplate);
    }

    @Override
    public CouponTemplate retrieveCouponTemplate(Long couponTemplateNum) {
        return couponTemplateRepository.findById(couponTemplateNum)
            .orElseThrow(() -> new NotFoundException(CouponTemplate.class, couponTemplateNum + "번 템플릿이 존재하지 않습니다."));
    }

    @Override
    public Page<CouponTemplateRetrieveResponse> retrieveAllCouponTemplate(Pageable pageable) {
        return couponTemplateRepository.findAllBy(pageable);
    }

    @Override
    public CouponTemplateDetailRetrieveResponse retrieveCouponTemplateDetailResponse(
        Long couponTemplateId) {
        return CouponTemplateDetailRetrieveResponse.fromEntity(couponTemplateRepository.findById(couponTemplateId).orElseThrow(() -> new IllegalArgumentException("No Such CouponTemplate.")));
    }

    @Override
    public CouponTemplate updateCouponTemplate(
        CouponTemplateUpdateRequest couponTemplateUpdateRequest){
        couponService.retrieveCoupon(couponTemplateUpdateRequest.getId());
        couponTypeService.retrieveCouponType(couponTemplateUpdateRequest.getTypeCode());
        //todo imageService 완성시 변경
        CouponTemplate couponTemplate = couponTemplateUpdateRequest.toEntity(0L);
        return couponTemplateRepository.save(couponTemplate);
    }

    @Override
    public void deleteCouponTemplate(Long couponTemplateId) {
        if(!couponTemplateRepository.existsById(couponTemplateId)) {
            throw new NotFoundException(CouponTemplate.class, couponTemplateId + "번 템플릿이 존재하지 않습니다.");
        }
        couponTemplateRepository.deleteById(couponTemplateId);
    }
}
