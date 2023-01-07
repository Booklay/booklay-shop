package com.nhnacademy.booklay.server.repository.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.repository.MemberRepository;
import com.nhnacademy.booklay.server.repository.OrderRepository;
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
class DeliveryDetailRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    DeliveryDetailRepository deliveryDetailRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    DeliveryDetail deliveryDetail;

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

        clearRepo("member", memberRepository);
        clearRepo("order", orderRepository);
        clearRepo("delivery_detail", deliveryDetailRepository);

        //given
        deliveryDetail = Dummy.getDummyDeliveryDetail();

        entityManager.persist(deliveryDetail.getOrder().getMember().getGender());
        memberRepository.save(deliveryDetail.getOrder().getMember());

        entityManager.persist(deliveryDetail.getOrder().getOrderStatusCode());
        orderRepository.save(deliveryDetail.getOrder());

        entityManager.persist(deliveryDetail.getStatusCode());
    }

    @Test
    @DisplayName("DeliveryDetail save test ")
    void testDeliveryDetailSave() {
        //given

        //when
        DeliveryDetail expected = deliveryDetailRepository.save(deliveryDetail);

        //then
        assertThat(expected.getId()).isEqualTo(deliveryDetail.getId());
    }

    @Test
    @DisplayName("DeliveryDetail findById test ")
    void testDeliveryDetailFindById() {
        //given
        deliveryDetailRepository.save(deliveryDetail);

        //when
        DeliveryDetail expected = deliveryDetailRepository.findById(deliveryDetail.getId())
            .orElseThrow(() -> new IllegalArgumentException("Delivery Detail not found"));
        //then
        assertThat(expected.getId()).isEqualTo(deliveryDetail.getId());
    }

}