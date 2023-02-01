package com.nhnacademy.booklay.server.repository.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
    DeliveryDestinationCURequest deliveryDestinationCURequest;

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
        clearRepo("member", memberRepository);

        //given
        deliveryDestination = Dummy.getDummyDeliveryDestination();
        deliveryDestinationCURequest = Dummy.getDummyDeliveryDestinationCreateRequest();

        entityManager.persist(deliveryDestination.getMember().getGender());
        memberRepository.save(deliveryDestination.getMember());
    }

    @Test
    @DisplayName("DeliveryDestination save success test ")
    void DeliveryDestinationSaveSuccessTest() {
        //given

        //when
        DeliveryDestination expected = deliveryDestinationRepository.save(deliveryDestination);

        //then
        assertThat(expected.getId()).isEqualTo(deliveryDestination.getId());
    }

    @Disabled
    @Test
    @DisplayName("findById success test")
    void deliveryDestinationFindBySuccessTest() {
        //given
        deliveryDestinationRepository.save(deliveryDestination);

        //when
        DeliveryDestination expected = deliveryDestinationRepository.findById(
            deliveryDestination.getId()).orElseThrow(() -> new IllegalArgumentException());

        //then
        assertThat(expected.getId()).isEqualTo(deliveryDestination.getId());
    }

    @Test
    @DisplayName("retrieveDeliveryDestinationByMemberNo success test")
    void retrieveDeliveryDestinationByMemberNoSuccessTest() {
        //given
        deliveryDestinationRepository.save(deliveryDestination);

        //when
        List<DeliveryDestinationRetrieveResponse> expected =
            deliveryDestinationRepository.retrieveDeliveryDestinationByMemberNo(
                deliveryDestination.getMember().getMemberNo());

        assertThat(expected).isNotNull();
    }

    @Test
    @DisplayName("findByIsDefaultDestination success test")
    void findByIsDefaultDestinationSuccessTest() {
        //given
        deliveryDestinationRepository.save(deliveryDestination);

        //when
        DeliveryDestination expected =
            deliveryDestinationRepository.findByIsDefaultDestination(
                    deliveryDestination.getIsDefaultDestination())
                .orElseThrow(() -> new IllegalArgumentException());

        assertThat(expected.getId()).isEqualTo(deliveryDestination.getId());
    }

    @Disabled
    @Test
    @DisplayName("deleteAllByMember_MemberNo success test")
    void deleteAllByMember_MemberNoSuccessTest() {
        //given
        deliveryDestinationRepository.save(deliveryDestination);
        //when
        DeliveryDestination expected =
            deliveryDestinationRepository.findByIsDefaultDestination(
                    deliveryDestination.getIsDefaultDestination())
                .orElseThrow(() -> new IllegalArgumentException());

        assertThat(expected.getId()).isEqualTo(deliveryDestination.getId());
    }
}