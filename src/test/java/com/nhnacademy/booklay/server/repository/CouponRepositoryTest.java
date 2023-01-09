package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.nhnacademy.booklay.server.dto.coupon.CouponRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class CouponRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CouponRepository couponRepository;

    @Test
    @DisplayName("CouponRepository save test")
    void testCouponSave() {

        //given
        Coupon coupon = Dummy.getDummyCoupon();
        entityManager.persist(coupon.getImage());
        entityManager.persist(coupon.getCouponType());

        //when
        Coupon expected = couponRepository.save(coupon);

        //then
        assertThat(expected.getName()).isEqualTo(coupon.getName());
    }

    @Test
    @DisplayName("CouponRepository findById() test")
    void testCouponFindById() {

        //given
        Coupon coupon = Dummy.getDummyCoupon();
        entityManager.persist(coupon.getImage());
        entityManager.persist(coupon.getCouponType());
        couponRepository.save(coupon);

        //when
        Coupon expected = couponRepository.findById(coupon.getId()).orElseThrow(() -> new IllegalArgumentException("Coupon not found."));

        //then
        assertThat(expected.getId()).isEqualTo(coupon.getId());
    }

    @Test
    @DisplayName("CouponRepository")
    void testGetCoupons() {
/*
        //given
        Coupon coupon = Dummy.getDummyCoupon();
        entityManager.persist(coupon.getImage());
        entityManager.persist(coupon.getCouponType());
        couponRepository.save(coupon);

        //when
        List<CouponRetrieveResponse> dtoList = couponRepository.getCouponsDto();

        //then
        assertThat(dtoList.size()).isEqualTo(1);
        assertThat(dtoList.get(0).getName()).isEqualTo(coupon.getName());*/
    }

    @Test
    @DisplayName("CouponRepository deleteCoupon() test.")
    void testDeleteCoupon() {

        //given
        Coupon coupon = Dummy.getDummyCoupon();
        entityManager.persist(coupon.getImage());
        entityManager.persist(coupon.getCouponType());
        couponRepository.save(coupon);

        //when
        couponRepository.deleteById(coupon.getId());

        //then
        assertThat(couponRepository.findAll().size()).isEqualTo(0);
    }
}