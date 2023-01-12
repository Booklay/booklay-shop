package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponTypeCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponTypeRetrieveResponse;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.exception.category.NotFoundException;
import com.nhnacademy.booklay.server.repository.coupon.CouponTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponTypeServiceImpl implements CouponTypeService{

    private final CouponTypeRepository couponTypeRepository;

    @Override
    public void createCouponType(CouponTypeCURequest couponTypeRequest) {
        couponTypeRepository.save(CouponTypeCURequest.toEntity(couponTypeRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponTypeRetrieveResponse> retrieveAllCouponTypes(Pageable pageable) {
        return couponTypeRepository.findAllBy(pageable);
    }

    @Override
    public void updateCouponType(Long couponTypeId, CouponTypeCURequest couponTypeRequest) {
        if(!couponTypeRepository.existsById(couponTypeId)) {
            throw new NotFoundException(CouponType.class.toString(), couponTypeId);
        }

        CouponType couponType = CouponType.builder()
            .id(couponTypeRequest.getId())
            .name(couponTypeRequest.getName())
            .build();

        couponTypeRepository.save(couponType);
    }

    @Override
    public void deleteCouponType(Long couponTypeId) {
        if(!couponTypeRepository.existsById(couponTypeId)) {
            throw new NotFoundException(CouponType.class.toString(), couponTypeId);
        }
        couponTypeRepository.deleteById(couponTypeId);
    }


}
