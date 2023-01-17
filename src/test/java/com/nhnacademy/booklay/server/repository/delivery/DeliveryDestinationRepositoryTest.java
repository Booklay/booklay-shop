package com.nhnacademy.booklay.server.repository.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCreateRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class DeliveryDestinationRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    DeliveryDestinationRepository deliveryDestinationRepository;

    @Autowired
    MemberRepository memberRepository;

    DeliveryDestination deliveryDestination;
    DeliveryDestinationCreateRequest deliveryDestinationCreateRequest;

    void clearRepo(String entityName, JpaRepository jpaRepository) {
        jpaRepository.deleteAll();

        String query =
            String.format("ALTER TABLE `%s` ALTER COLUMN `%s_no` RESTART WITH 1", entityName,
                entityName);

        this.entityManager
            .getEntityManager()
            .createNativeQuery(query)
            .executeUpdate();
    }

    @BeforeEach
    void setUp() {

        clearRepo("delivery_destination", deliveryDestinationRepository);
        clearRepo("member_no", memberRepository);

        //given
        deliveryDestination = Dummy.getDummyDeliveryDestination();
        deliveryDestinationCreateRequest = Dummy.getDummyDeliveryDestinationCreateRequest();

        entityManager.persist(deliveryDestination.getMember().getGender());
        memberRepository.save(deliveryDestination.getMember());
    }

    @Test
    @DisplayName("DeliveryDestination save test ")
    void testDeliveryDestinationSave() {
        //given

        //when
        DeliveryDestination expected = deliveryDestinationRepository.save(deliveryDestination);

        //then
        assertThat(expected.getId()).isEqualTo(deliveryDestination.getId());
    }

    @Test
    void testDeliveryDestinationFindAllByMember_MemberNo() {

    }

    @Test
    void testDeliveryDestinationCountByMember_MemberNo() {
    }
}