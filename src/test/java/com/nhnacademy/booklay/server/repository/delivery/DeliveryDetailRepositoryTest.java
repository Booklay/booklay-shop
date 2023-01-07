package com.nhnacademy.booklay.server.repository.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.repository.MemberRepository;
import com.nhnacademy.booklay.server.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

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

    @BeforeEach
    void setUp() {
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