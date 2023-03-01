package com.nhnacademy.booklay.server.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthController.class)
@MockBean(JpaMetamodelMappingContext.class)
class HealthControllerTest {

    @Autowired
    HealthController healthController;

    @Autowired
    MockMvc mockMvc;

    @Test
    void healthCheck() throws Exception {
        mockMvc.perform(get("/health")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();
    }

    @Test
    void healthCheck_notHelty() throws Exception {
        ReflectionTestUtils.setField(healthController, "healthStatus", false);
        mockMvc.perform(get("/health")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();
    }
}