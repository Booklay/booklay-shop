package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTemplateCreateRequest;
import com.nhnacademy.booklay.server.entity.CouponTemplate;
import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.coupon.CouponTemplateRepository;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponTemplateServiceImpl implements CouponTemplateService{
    private final CouponTemplateRepository couponTemplateRepository;
    @Override
    public CouponTemplate createCouponTemplate(
        @Valid CouponTemplateCreateRequest couponTemplateCreateRequest) {
        //todo imageService 완성시 변경
        Image
            dummyImage = Image.builder().id(1L).address(couponTemplateCreateRequest.getImagePath()).build();
        CouponTemplate couponTemplate = couponTemplateCreateRequest.toEntity(dummyImage.getId());
        return couponTemplateRepository.save(couponTemplate);
    }

    @Override
    public CouponTemplate retrieveCouponTemplate(Long couponTemplateNum) {
        return couponTemplateRepository.findById(couponTemplateNum)
            .orElseThrow(() -> new NotFoundException(CouponTemplate.class, couponTemplateNum + "번 템플릿이 존재하지 않습니다."));
    }
}
