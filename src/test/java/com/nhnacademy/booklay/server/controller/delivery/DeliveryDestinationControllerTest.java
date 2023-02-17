package com.nhnacademy.booklay.server.controller.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.service.delivery.DeliveryDestinationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DeliveryDestinationController.class)
@MockBean(JpaMetamodelMappingContext.class)
class DeliveryDestinationControllerTest {
    @MockBean
    DeliveryDestinationService deliveryDestinationService;

    @Autowired
    DeliveryDestinationController deliveryDestinationController;

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    void createDeliveryDestinationSuccessTest() throws Exception {

    }
}