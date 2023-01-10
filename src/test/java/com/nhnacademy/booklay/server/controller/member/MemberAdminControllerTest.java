package com.nhnacademy.booklay.server.controller.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.booklay.server.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * author 양승아
 *
 */
@WebMvcTest(MemberAdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberAdminControllerTest {

    @MockBean
    MemberService memberService;

    @Autowired
    MemberAdminController memberAdminController;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Controller 연결 테스트")
    void testMapping() throws Exception {
        //then
        mockMvc.perform(get("/admin/members"))
            .andExpect(status().isOk())
            .andReturn();
    }

//    @Test
//    @DisplayName("관리자의 회원리스트 검색 테스트")
//    void retrieveMembers() throws Exception {
//        //given
//        Page<MemberRetrieveResponse> page = Page.empty();
//
//        //mocking
//        when(memberService.getMembers(Pageable.unpaged())).thenReturn(
//            (List<MemberRetrieveResponse>) page);
//
//        //then
//        mockMvc.perform(get("/admin/members"))
//            .andExpect(status().isOk())
//            .andDo(print())
//            .andReturn();
//    }

    @Test
    void retrieveMember() {
    }
}
