package com.nhnacademy.booklay.server.controller.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.service.member.MemberService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author 양승아
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

    Member member;
    MemberRetrieveResponse memberRetrieveResponse;

    private static final String URI_PREFIX = "/admin/members";

    @BeforeEach
    void setUp() {
        member = Dummy.getDummyMember();

        memberRetrieveResponse =
            new MemberRetrieveResponse(member.getMemberNo(), member.getGender().getName(),
                member.getMemberId(), member.getNickname(), member.getName(), member.getBirthday(),
                member.getPhoneNo(), member.getEmail(), member.getCreatedAt(),
                member.getUpdatedAt(), member.getDeletedAt(), member.getIsBlocked());
    }

    @Test
    @DisplayName("관리자의 회원리스트 검색 테스트")
    void testRetrieveMembers() throws Exception {
        //given
        PageImpl<MemberRetrieveResponse> page = new PageImpl<>(List.of());

        //mocking
        when(memberService.retrieveMembers(any())).thenReturn(page);

        //then
        mockMvc.perform(get("/admin/members")).andExpect(status().isOk()).andDo(print()).andReturn();
    }

    @Test
    @DisplayName("관리자의 단일회원 조회 성공 테스트")
    void retrieveMemberTest_Success() throws Exception {
        //mocking
        when(memberService.retrieveMember(member.getMemberNo())).thenReturn(memberRetrieveResponse);

        //then
        mockMvc.perform(get(URI_PREFIX + "/" + member.getMemberNo()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print()).andReturn();
    }

    //    @Test
    @DisplayName("관리자의 단일회원 조회 실패 테스트")
    void retrieveMemberTest_ifNotExistMemberId() throws Exception {
        //mocking
        when(memberService.retrieveMember(member.getMemberNo())).thenThrow(MemberNotFoundException.class);

        //then
        mockMvc.perform(get(URI_PREFIX + "/" + member.getMemberNo()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andDo(print()).andReturn();
    }


}
