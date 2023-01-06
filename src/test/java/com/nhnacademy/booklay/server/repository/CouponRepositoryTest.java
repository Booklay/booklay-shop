package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Coupon;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class CouponRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    CouponRepository couponRepository;

    @Test
    @DisplayName("couponRepository save test")
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
    @DisplayName("couponRepository save test")
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
}