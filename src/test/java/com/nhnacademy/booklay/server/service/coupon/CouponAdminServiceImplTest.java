package com.nhnacademy.booklay.server.service.coupon;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CouponAdminServiceImplTest {

    @InjectMocks
    private CouponAdminServiceImpl couponAdminService;

    @Mock
    private CouponRepository couponRepository;

    @Test
    @DisplayName("test retrieveAllCoupons()")
    void testRetrieveAllCoupons() {

        // given
        List<Coupon> couponList = List.of(Dummy.getDummyCoupon());
        when(couponRepository.findAll()).thenReturn(couponList);

        // when
        List<CouponRetrieveResponse> expected = couponAdminService.retrieveAllCoupons();

        // then
        assertThat(expected.size()).isEqualTo(couponList.size());
        assertThat(expected.get(0).getName()).isEqualTo(couponList.get(0).getName());

        BDDMockito.then(couponRepository).should().findAll();
    }

    @Test
    @DisplayName("test retrieveCoupon()")
    void testRetrieveCoupon() {

        // given
        Coupon coupon = Dummy.getDummyCoupon();
        when(couponRepository.findById(1L)).thenReturn(Optional.ofNullable(coupon));

        // when
        CouponDetailRetrieveResponse expected = couponAdminService.retrieveCoupon(1L);

        // then
        assertThat(expected.getName()).isEqualTo(coupon.getName());
        assertThat(expected.getTypeName()).isEqualTo(coupon.getCouponType().getName());

        BDDMockito.then(couponRepository).should().findById(1L);
    }
}