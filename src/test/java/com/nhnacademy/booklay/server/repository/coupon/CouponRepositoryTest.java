package com.nhnacademy.booklay.server.repository.coupon;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Coupon;
import com.nhnacademy.booklay.server.repository.coupon.CouponRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class CouponRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CouponRepository couponRepository;

    Coupon coupon;

    void clearRepo(String entityName, JpaRepository jpaRepository) {
        jpaRepository.deleteAll();

        String query = String.format("ALTER TABLE `%s` ALTER COLUMN `%s_no` RESTART WITH 1", entityName, entityName);

        this.entityManager
            .getEntityManager()
            .createNativeQuery(query)
            .executeUpdate();
    }

    @BeforeEach
    void setUp() {
        clearRepo("coupon", couponRepository);
        coupon = Dummy.getDummyCoupon();

        entityManager.persist(coupon.getImage());
        entityManager.persist(coupon.getCouponType());

        couponRepository.save(coupon);
    }

    @Test
    @DisplayName("CouponRepository save test")
    void testCouponSave() {

        //given

        //when
        Coupon expected = couponRepository.save(coupon);

        //then
        assertThat(expected.getName()).isEqualTo(coupon.getName());
    }

    @Test
    @DisplayName("findById() - 쿠폰 단건 조회 테스트")
    void testCouponFindById() {

        //given

        //when
        Coupon expected = couponRepository.findById(coupon.getId()).orElseThrow(() -> new IllegalArgumentException("Coupon not found."));

        //then
        assertThat(expected.getId()).isEqualTo(coupon.getId());
    }

    @Test
    @DisplayName("getCoupons() - 쿠폰 리스트 조회 테스트")
    void testGetCoupons() {

        //given

        //when
        List<Coupon> dtoList = couponRepository.findAll();

        //then
        assertThat(dtoList.size()).isEqualTo(1);
        assertThat(dtoList.get(0).getName()).isEqualTo(coupon.getName());
    }

    @Test
    @Order(1)
    @DisplayName("deleteCoupon() - 쿠폰 삭제 테스트")
    void testDeleteCoupon() {

        //given

        //when
        System.out.println(couponRepository.findAll());
        couponRepository.deleteById(coupon.getId());

        //then
        assertThat(couponRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("findAllBy() - Page<Coupon> 조회 테스트")
    void testFindAllBy() {

        // given
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<Coupon> couponPage = couponRepository.findAllBy(pageRequest);

        // then
        assertThat(couponPage).isNotNull();

    }
}