package com.nhnacademy.booklay.server.service.coupon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.CouponType;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import com.nhnacademy.booklay.server.repository.coupon.CouponTypeRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CouponAdminServiceImplTest {

    @InjectMocks
    private CouponAdminServiceImpl couponAdminService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponTypeRepository couponTypeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    Coupon coupon;
    List<Coupon> couponList;
    CouponType couponType;
    CouponCreateRequest couponCreateCoupon;
    CouponUpdateRequest couponUpdateRequest;

    @BeforeEach
    void setUp() {
        coupon = Dummy.getDummyCoupon();
        couponList = List.of(coupon);
        couponType = Dummy.getDummyCouponType();
        couponCreateCoupon = Dummy.getDummyCouponCreateRequest();
        couponUpdateRequest = Dummy.getDummyCouponUpdateRequest();
    }

    @Test
    @DisplayName("쿠폰 생성 테스트")
    void testCreateCoupon() {

        // given
        given(couponTypeRepository.findById(couponCreateCoupon.getTypeCode())).willReturn(
            Optional.ofNullable(Dummy.getDummyCouponType()));

        given(categoryRepository.findById(couponCreateCoupon.getApplyItemId())).willReturn(
            Optional.ofNullable(Dummy.getDummyCategory()));

        // when
        couponAdminService.createCoupon(couponCreateCoupon);

        // then
        BDDMockito.then(couponRepository).should().save(any());
    }

    @Test
    @DisplayName("모든 쿠폰 조회 테스트")
    void testRetrieveAllCoupons() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        given(couponRepository.findAllBy(pageRequest)).willReturn(Page.empty());

        // when
        couponAdminService.retrieveAllCoupons(pageRequest);

        // then
        BDDMockito.then(couponRepository).should().findAllBy(pageRequest);
    }

    @Test
    @DisplayName("쿠폰 단건 조회 테스트")
    void testRetrieveCoupon() {

        // given
        Long targetId = 1L;
        given(couponRepository.findById(targetId)).willReturn(Optional.ofNullable(coupon));

        // when
        couponAdminService.retrieveCoupon(targetId);

        // then
        BDDMockito.then(couponRepository).should().findById(targetId);
    }

    @Test
    @DisplayName("쿠폰 수정 테스트")
    void testUpdateCoupon() {

        // given
        Long targetId = coupon.getId();

        given(couponRepository.findById(targetId)).willReturn(Optional.ofNullable(coupon));
        given(couponTypeRepository.findById(couponUpdateRequest.getTypeCode())).willReturn(Optional.ofNullable(couponType));
        given(productRepository.findById(targetId)).willReturn(
            Optional.ofNullable(DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto())));

        // when
        couponAdminService.updateCoupon(coupon.getId(), couponUpdateRequest);

        // then
        BDDMockito.then(couponRepository).should().save(any());
    }

    @Test
    @DisplayName("쿠폰 삭제 테스트")
    void testDeleteCoupon() {

        // given
        Long targetId = 1L;
        given(couponRepository.existsById(targetId)).willReturn(true);

        // when
        couponAdminService.deleteCoupon(targetId);

        // then
        BDDMockito.then(couponRepository).should().deleteById(targetId);
    }
}