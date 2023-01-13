package com.nhnacademy.booklay.server.repository.coupon;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.CouponType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class CouponTypeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CouponTypeRepository couponTypeRepository;

    @Test
    @DisplayName("쿠폰 타입 save 테스트")
    void testOrderCouponSave() {

        // given
        CouponType couponType = Dummy.getDummyCouponType();

        // when
        CouponType expected = couponTypeRepository.save(couponType);

        // then
        assertThat(expected).isNotNull();

    }

    @Test
    @DisplayName("주문쿠폰 findById() 테스트")
    void testOrderCouponFindById() {

        // given
        CouponType couponType = Dummy.getDummyCouponType();

        // when
        CouponType expected = couponTypeRepository.save(couponType);

        // then
        assertThat(expected.getId()).isEqualTo(couponType.getId());
    }
}
