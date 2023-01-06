package com.nhnacademy.booklay.server.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.DummyNum3;
import com.nhnacademy.booklay.server.entity.OwnedEbook;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class OwnedEbookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    OwnedEbookRepository ownedEbookRepository;


    @Test
    @DisplayName("RestockingNotificationRepository save test ")
    void testRestockingNotificationSave() {
        //given
        OwnedEbook ownedEbook = DummyNum3.getDummyOwnedEbook();
        entityManager.persist(ownedEbook.getProduct().getImage());
        entityManager.persist(ownedEbook.getProduct());
        entityManager.persist(ownedEbook.getMember().getGender());
        entityManager.persist(ownedEbook.getMember());

        //when
        OwnedEbook expected = ownedEbookRepository.save(ownedEbook);
        //then
        assertThat(expected.getProduct()).isEqualTo(ownedEbook.getProduct());

    }



    @Test
    @DisplayName("RestockingNotificationRepository findById 테스트")
    void testMemberFindById() {
        //given
        OwnedEbook ownedEbook = DummyNum3.getDummyOwnedEbook();
        entityManager.persist(ownedEbook.getProduct().getImage());
        entityManager.persist(ownedEbook.getProduct());
        entityManager.persist(ownedEbook.getMember().getGender());
        entityManager.persist(ownedEbook.getMember());
        ownedEbookRepository.save(ownedEbook);

        //when
        OwnedEbook expected = ownedEbookRepository.findById(ownedEbook.getId()).orElseThrow(() -> new IllegalArgumentException("RestockingNotification not found"));

        //then
        assertThat(expected.getMember()).isEqualTo(ownedEbook.getMember());
    }


}
