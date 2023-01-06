package com.nhnacademy.booklay.server.repository;

import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class CouponRepositoryTest {

    @Autowired
    CouponRepository couponRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCouponSave() {
    }

}