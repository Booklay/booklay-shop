package com.nhnacademy.booklay.server.service.coupon;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.coupon.CouponCURequest;
import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
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
    CouponCURequest couponRequest;

    @BeforeEach
    void setUp() {
        coupon = Dummy.getDummyCoupon();
        couponList = List.of(coupon);
        couponRequest = Dummy.getDummyCouponCURequest();
    }

    @Test
    @DisplayName("createCoupon() - 쿠폰 생성 테스트")
    void testCreateCoupon() {

        // given
        when(couponTypeRepository.findById(1L)).thenReturn(
            Optional.ofNullable(Dummy.getDummyCouponType()));

        // when
        couponAdminService.createCoupon(couponRequest);

        // then
        BDDMockito.then(couponRepository).should().save(any());
    }

    @Test
    @DisplayName("retrieveAllCoupons() - 모든 쿠폰 조회 테스트")
    void testRetrieveAllCoupons() {
/*
        // given
        couponRepository.save(coupon);

        // when
        List<CouponRetrieveResponse> expected = couponAdminService.retrieveAllCoupons(0);

        // then
        assertThat(expected.size()).isEqualTo(couponList.size());
        assertThat(expected.get(0).getName()).isEqualTo(couponList.get(0).getName());

        BDDMockito.then(couponRepository).should().findAll();*/
    }

    @Test
    @DisplayName("retrieveCoupon() - 쿠폰 단건 조회 테스트")
    void testRetrieveCoupon() {

        // given
        Coupon coupon = Dummy.getDummyCoupon();
        when(couponRepository.findById(1L)).thenReturn(Optional.ofNullable(coupon));

        // when
        CouponDetailRetrieveResponse expected = couponAdminService.retrieveCoupon(1L);

        // then
        BDDMockito.then(couponRepository).should().findById(1L);
    }

    @Test
    @DisplayName("deleteCoupon() - 쿠폰 삭제 테스트")
    void testDeleteCoupon() {

        // given

        // when
        couponAdminService.deleteCoupon(1L);

        // then
        BDDMockito.then(couponRepository).should().deleteById(1L);
    }
}