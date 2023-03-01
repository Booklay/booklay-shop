package com.nhnacademy.booklay.server.controller.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.booklay.server.controller.admin.member.MemberAdminController;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.service.OauthService;
import com.nhnacademy.booklay.server.service.member.MemberService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OauthController.class)
@MockBean(JpaMetamodelMappingContext.class)
class OauthControllerTest {

    @Autowired
    OauthController oauthController;

    @MockBean
    OauthService oauthService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void checkOauthUser() throws Exception {
        //given

        //mocking

        //then
        mockMvc.perform(get("/ouath/checked/user")
                .queryParam("id", "1")
                .queryParam("email", "test@test.com"))
            .andExpect(status().isOk()).andDo(print())
            .andReturn();

        BDDMockito.then(oauthService).should().checkUser(anyString(), anyString());
    }
}