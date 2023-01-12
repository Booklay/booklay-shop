package com.nhnacademy.booklay.server.repository.delivery;

import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.DeliveryStatusCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class DeliveryStatusCodeRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    DeliveryStatusCodeRepository deliveryStatusCodeRepository;

    @Test
    @DisplayName("DeliveryStatusCode save test")
    void testDeliveryStatusCodeSave() {
        //given
        DeliveryStatusCode deliveryStatusCode =
            Dummy.getDummyDeliveryStatusCode();


        //when
        DeliveryStatusCode expected = deliveryStatusCodeRepository.save(deliveryStatusCode);

        //then
        assertThat(expected.getName()).isEqualTo(deliveryStatusCode.getName());
    }

    @Test
    @DisplayName("DeliveryStatusCode findById test")
    void testDeliveryStatusCodeFindById() {
        //given
        DeliveryStatusCode deliveryStatusCode =
            Dummy.getDummyDeliveryStatusCode();


        deliveryStatusCodeRepository.save(deliveryStatusCode);

        //when
        DeliveryStatusCode expected =
            deliveryStatusCodeRepository.findById(deliveryStatusCode.getId())
                .orElseThrow(() -> new IllegalArgumentException("Code not found"));

        //then
        assertThat(expected.getName()).isEqualTo(deliveryStatusCode.getName());
    }


}
