package com.nhnacademy.booklay.server.service.coupon;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.coupon.CouponDetailRetrieveResponse;
import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CouponAdminServiceImplTest {

    @Autowired
    private CouponAdminService couponAdminService;

    @MockBean
    private CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("test retrieveAllCoupons()")
    void testRetrieveAllCoupons() {
        List<Coupon> couponList = List.of(Dummy.getDummyCoupon());
        when(couponRepository.findAll()).thenReturn(couponList);

        List<CouponRetrieveResponse> expected = couponAdminService.retrieveAllCoupons();

        assertThat(expected.size()).isEqualTo(couponList.size());
        assertThat(expected.get(0).getName()).isEqualTo(couponList.get(0).getName());
    }

    @Test
    @DisplayName("test retrieveCoupon()")
    void testRetrieveCoupon() {
        Coupon coupon = Dummy.getDummyCoupon();
        when(couponRepository.findById(1L)).thenReturn(Optional.ofNullable(coupon));

        CouponDetailRetrieveResponse expected = couponAdminService.retrieveCoupon(1L);

        assertThat(expected.getName()).isEqualTo(coupon.getName());
        assertThat(expected.getTypeName()).isEqualTo(coupon.getCouponType().getName());
    }
}