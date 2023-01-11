package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.exception.category.NotFoundException;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import com.nhnacademy.booklay.server.repository.coupon.CouponTypeRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author 김승혜
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CouponAdminServiceImpl implements CouponAdminService{

    private final CouponRepository couponRepository;
    private final CouponTypeRepository couponTypeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void createCoupon(@Valid CouponCreateRequest couponRequest) {
        CouponType couponType = couponTypeRepository.findById(couponRequest.getTypeCode()).orElseThrow(() -> new IllegalArgumentException("No Such Coupon Type."));

        Coupon coupon = CouponCreateRequest.toEntity(couponRequest, couponType);

        Long applyItemId = couponRequest.getApplyItemId();
        setCategoryOrProduct(coupon, couponRequest.getIsOrderCoupon(), applyItemId);

        couponRepository.save(coupon);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponRetrieveResponse> retrieveAllCoupons(Pageable pageable) {
        return couponRepository.findAllBy(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponDetailRetrieveResponse retrieveCoupon(Long couponId) {
        return CouponDetailRetrieveResponse.fromEntity(couponRepository.findById(couponId).orElseThrow(() -> new IllegalArgumentException("No Such Coupon.")));
    }

    @Override
    public void updateCoupon(Long couponId, CouponUpdateRequest couponRequest) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(Coupon.class.toString(), couponId));
        CouponType couponType = couponTypeRepository.findById(couponRequest.getTypeCode()).orElseThrow(() -> new NotFoundException(CouponType.class.toString(), couponRequest.getTypeCode()));

        coupon.update(couponRequest, couponType);
        setCategoryOrProduct(coupon, couponRequest.getIsOrderCoupon(), couponRequest.getApplyItemId());

        couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Long couponId) {
        if(!couponRepository.existsById(couponId)) {
            throw new NotFoundException(Coupon.class.toString(), couponId);
        }
        couponRepository.deleteById(couponId);
    }

    private void setCategoryOrProduct(Coupon coupon, boolean isOrderCoupon, Long applyItemId) {
        if(isOrderCoupon) {
            Category category = categoryRepository.findById(applyItemId)
                .orElseThrow(() -> new NotFoundException(Category.class.toString(), applyItemId));

            coupon.setCategory(category);
        } else {
            Product product = productRepository.findById(applyItemId)
                .orElseThrow(() -> new NotFoundException(Product.class.toString(), applyItemId));

            coupon.setProduct(product);
        }
    }

}
