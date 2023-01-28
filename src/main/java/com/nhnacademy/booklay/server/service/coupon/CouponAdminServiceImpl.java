package com.nhnacademy.booklay.server.service.coupon;

import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponIssueRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.OrderCoupon;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductCoupon;
import com.nhnacademy.booklay.server.exception.category.NotFoundException;
import com.nhnacademy.booklay.server.repository.ImageRepository;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import com.nhnacademy.booklay.server.repository.coupon.CouponTypeRepository;
import com.nhnacademy.booklay.server.repository.coupon.OrderCouponRepository;
import com.nhnacademy.booklay.server.repository.coupon.ProductCouponRepository;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 김승혜
 */
@Service
@Transactional
@RequiredArgsConstructor
public class CouponAdminServiceImpl implements CouponAdminService {

    private final CouponRepository couponRepository;
    private final CouponTypeRepository couponTypeRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderCouponRepository orderCouponRepository;
    private final ProductCouponRepository productCouponRepository;
    private final ImageRepository imageRepository;

    @Override
    public void createCoupon(CouponCreateRequest couponRequest) {

        Long typeCode = couponRequest.getTypeCode();
        Long imageId = couponRequest.getImageId();

        CouponType couponType = couponTypeRepository.findById(typeCode)
            .orElseThrow(() -> new NotFoundException(CouponType.class.toString(), typeCode));

        Image image = imageRepository.findById(imageId)
            .orElseThrow(() -> new NotFoundException(Image.class.toString(), imageId));

        Coupon coupon = CouponCreateRequest.toEntity(couponRequest, couponType, image);

        // 수량을 정하지 않으면, 수량 제한이 없음.
        if (couponRequest.getQuantity() == null) {
            coupon.setIsLimited(false);
        } else {
            coupon.setIsLimited(true);
        }

        setCategoryOrProduct(coupon, couponRequest.getIsOrderCoupon(),
            couponRequest.getApplyItemId());

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
        return CouponDetailRetrieveResponse.fromEntity(couponRepository.findById(couponId)
            .orElseThrow(() -> new IllegalArgumentException("No Such Coupon.")));
    }

    @Override
    public void updateCoupon(Long couponId, CouponUpdateRequest couponRequest) {
        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new NotFoundException(Coupon.class.toString(), couponId));

        CouponType couponType = couponTypeRepository.findById(couponRequest.getTypeCode())
            .orElseThrow(() -> new NotFoundException(CouponType.class.toString(),
                couponRequest.getTypeCode()));

        coupon.update(couponRequest, couponType);
        setCategoryOrProduct(coupon, couponRequest.getIsOrderCoupon(),
            couponRequest.getApplyItemId());

        couponRepository.save(coupon);
    }

    @Override
    public void deleteCoupon(Long couponId) {
        if (!couponRepository.existsById(couponId)) {
            throw new NotFoundException(Coupon.class.toString(), couponId);
        }
        couponRepository.deleteById(couponId);
    }

    @Override
    public void issueCoupon(CouponIssueRequest couponRequest) {
        Long couponId = couponRequest.getCouponId();
        Long memberId = couponRequest.getMemberId();

        String code = UUID.randomUUID().toString().substring(0, 30);

        Coupon coupon = couponRepository.findById(couponId)
            .orElseThrow(() -> new NotFoundException(Coupon.class.toString(), couponId));
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(Member.class.toString(), memberId));


        if (Objects.nonNull(coupon.getCategory())) {
            OrderCoupon orderCoupon = new OrderCoupon(coupon, code);
            orderCoupon.setMember(member);
            orderCouponRepository.save(orderCoupon);
        } else if (Objects.nonNull(coupon.getProduct())) {
            ProductCoupon productCoupon = new ProductCoupon(coupon, code);
            productCoupon.setMember(member);
            productCouponRepository.save(productCoupon);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void setCategoryOrProduct(Coupon coupon, boolean isOrderCoupon, Long applyItemId) {
        if (isOrderCoupon) {
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
