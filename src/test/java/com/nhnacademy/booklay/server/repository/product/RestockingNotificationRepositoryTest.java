package com.nhnacademy.booklay.server.repository.product;


import com.nhnacademy.booklay.server.dummy.DummyNum3;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import com.nhnacademy.booklay.server.repository.mypage.RestockingNotificationRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class RestockingNotificationRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    RestockingNotificationRepository restockingNotificationRepository;
    @Disabled
    @Test
    @DisplayName("RestockingNotificationRepository save test ")
    void testRestockingNotificationSave() {
        //given
        RestockingNotification restockingNotification = DummyNum3.getDummyRestockingNotification();
        entityManager.persist(restockingNotification.getProduct().getObjectFile());
        entityManager.persist(restockingNotification.getProduct());
        entityManager.persist(restockingNotification.getMember().getGender());
        entityManager.persist(restockingNotification.getMember());

        //when
        RestockingNotification expected =
            restockingNotificationRepository.save(restockingNotification);
        //then
        assertThat(expected.getProduct()).isEqualTo(restockingNotification.getProduct());

    }

    @Disabled
    @Test
    @DisplayName("RestockingNotificationRepository findById 테스트")
    void testMemberFindById() {
        //given
        RestockingNotification restockingNotification = DummyNum3.getDummyRestockingNotification();
        entityManager.persist(restockingNotification.getProduct().getObjectFile());
        entityManager.persist(restockingNotification.getProduct());
        entityManager.persist(restockingNotification.getMember().getGender());
        entityManager.persist(restockingNotification.getMember());

        restockingNotificationRepository.save(restockingNotification);

        //when
        RestockingNotification expected =
            restockingNotificationRepository.findById(restockingNotification.getPk()).orElseThrow(
                () -> new IllegalArgumentException("RestockingNotification not found"));

        //then
        assertThat(expected.getPk()).isEqualTo(restockingNotification.getPk());
    }


}
