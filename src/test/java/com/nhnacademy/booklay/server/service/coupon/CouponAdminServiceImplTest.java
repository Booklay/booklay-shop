package com.nhnacademy.booklay.server.service.coupon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.coupon.CouponCreateRequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponUpdateRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import com.nhnacademy.booklay.server.repository.coupon.CouponTypeRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    Coupon coupon;
    List<Coupon> couponList;
    CouponCreateRequest couponCreateCoupon;
    CouponUpdateRequest couponUpdateRequest;

    @BeforeEach
    void setUp() {
        coupon = Dummy.getDummyCoupon();
        couponList = List.of(coupon);
        couponCreateCoupon = Dummy.getDummyCouponCreateRequest();
        couponUpdateRequest = Dummy.getDummyCouponUpdateRequest();
    }

    @Test
    @DisplayName("createCoupon() - 쿠폰 생성 서비스 테스트")
    void testCreateCoupon() {

        // given
        when(couponTypeRepository.findById(1L)).thenReturn(
            Optional.ofNullable(Dummy.getDummyCouponType()));

        // when
        couponAdminService.createCoupon(couponCreateCoupon);

        // then
        BDDMockito.then(couponRepository).should().save(any());
    }

    @Test
    @DisplayName("retrieveAllCoupons() - 모든 쿠폰 조회 서비스 테스트")
    void testRetrieveAllCoupons() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);
        when(couponRepository.findAllBy(pageRequest)).thenReturn(Page.empty());

        // when
        couponAdminService.retrieveAllCoupons(0);

        // then
        BDDMockito.then(couponRepository).should().findAllBy(pageRequest);
    }

    @Test
    @DisplayName("retrieveCoupon() - 쿠폰 단건 조회 서비스 테스트")
    void testRetrieveCoupon() {

        // given
        when(couponRepository.findById(1L)).thenReturn(Optional.ofNullable(coupon));

        // when
        CouponDetailRetrieveResponse expected = couponAdminService.retrieveCoupon(1L);

        // then
        BDDMockito.then(couponRepository).should().findById(1L);
    }

    @Test
    @DisplayName("updateCoupon() - 쿠폰 수정 서비스 테스트")
    void testUpdateCoupon() {

        // given
        when(couponRepository.findById(1L)).thenReturn(Optional.ofNullable(coupon));

        // when
        couponAdminService.updateCoupon(1L, Dummy.getDummyCouponUpdateRequest());

        // then
        BDDMockito.then(couponRepository).should().save(any());
    }

    @Test
    @DisplayName("deleteCoupon() - 쿠폰 삭제 서비스 테스트")
    void testDeleteCoupon() {

        // given
        when(couponRepository.existsById(1L)).thenReturn(true);

        // when
        couponAdminService.deleteCoupon(1L);

        // then
        BDDMockito.then(couponRepository).should().deleteById(1L);
    }
}