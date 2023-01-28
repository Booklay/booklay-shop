package com.nhnacademy.booklay.server.repository.coupon;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.entity.OrderCoupon;
import com.nhnacademy.booklay.server.exception.category.NotFoundException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class OrderCouponRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderCouponRepository orderCouponRepository;

    @Autowired
    private CouponRepository couponRepository;


    private Coupon coupon;
    private OrderCoupon orderCoupon;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
            .objectFile(DummyCart.getDummyFile())
            .couponType(Dummy.getDummyCouponType())
            .name("이달의 쿠폰")
            .amount(5)
            .minimumUseAmount(1000)
            .maximumDiscountAmount(3000)
            .issuanceDeadlineAt(LocalDateTime.of(2023, 1, 20, 0, 0, 0))
            .isDuplicatable(false)
            .build();

        coupon.setIsLimited(true);

        orderCoupon = OrderCoupon.builder()
            .coupon(coupon)
            .code(UUID.randomUUID().toString().substring(0, 30))
            .build();
    }

    @Test
    @DisplayName("주문쿠폰 save 테스트")
    void testOrderCouponSave() {

        // given
        entityManager.persist(coupon.getCouponType());
        entityManager.persist(coupon.getObjectFile());
        entityManager.persist(coupon);

        // when
        OrderCoupon expected = orderCouponRepository.save(orderCoupon);

        // then
        assertThat(expected).isNotNull();

    }

    @Test
    @DisplayName("주문쿠폰 findById() 테스트")
    void testOrderCouponFindById() {

        // given
        entityManager.persist(coupon.getCouponType());
        entityManager.persist(coupon.getObjectFile());
        entityManager.persist(coupon);
        orderCouponRepository.save(orderCoupon);

        // when
        OrderCoupon expected = orderCouponRepository.findById(orderCoupon.getId())
            .orElseThrow(() -> new NotFoundException(OrderCoupon.class.toString(),
                orderCoupon.getId()));

        // then
        assertThat(expected.getId()).isEqualTo(orderCoupon.getId());

    }
}
