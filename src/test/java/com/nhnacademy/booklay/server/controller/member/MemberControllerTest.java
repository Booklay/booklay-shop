package com.nhnacademy.booklay.server.controller.member;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.member.reponse.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @author 양승아
 */

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberControllerTest {

    @MockBean
    MemberService memberService;

    @Autowired
    MemberController memberController;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper;

    Member member;
    MemberRetrieveResponse responseDto;
    MemberCreateRequest createDto;
    MemberUpdateRequest updateDto;

    private static final String URI_PREFIX = "/members";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        member = Dummy.getDummyMember();

        responseDto = new MemberRetrieveResponse(
                member.getMemberNo(),
                member.getGender().getName(),
                member.getMemberId(),
                member.getPassword(),
                member.getNickname(),
                member.getName(),
                member.getBirthday(),
                member.getPhoneNo(),
                member.getEmail(),
                member.getCreatedAt(),
                member.getUpdatedAt(),
                member.getDeletedAt(),
                member.getIsBlocked());

        createDto = Dummy.getDummyMemberCreateRequest();

        updateDto = Dummy.getDummyMemberUpdateRequest();
    }

    @Test
    @DisplayName("회원의 본인정보 조회 성공 테스트")
    void testRetrieveMember() throws Exception {
        //given
        given(memberService.retrieveMember(anyLong())).willReturn(responseDto);

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/" + member.getMemberNo())
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$['id']").value(member.getMemberId()))
               .andExpect(jsonPath("$['name']").value("유재석"));

        then(memberService).should(times(1)).retrieveMember(member.getMemberNo());
    }


    @Test
    @DisplayName("회원의 본인정보 조회 실패시 제대로된 에러 메시지를 반환하는가 테스트")
    void testRetrieveMember_ifNotExistMemberNo_thenThrownValidMemberNotFoundErrorMessage() throws Exception {
        //given
        given(memberService.retrieveMember(anyLong())).willThrow(new MemberNotFoundException(member.getMemberNo()));

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/" + member.getMemberNo())
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$['message']").value("Member Not Found, MemberNo : 1"));

        then(memberService).should(times(1)).retrieveMember(anyLong());
    }

    @Test
    @DisplayName("회원 등록 성공 테스트")
    void testCreateMember() throws Exception {
        //given

        //when
        ResultActions result = mockMvc.perform(post(URI_PREFIX)
                .content(objectMapper.writeValueAsString(createDto))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isCreated());
        then(memberService).should(times(1)).createMember(any());
    }

    @Test
    @DisplayName("회원 수정 성공 테스트")
    void testUpdateMember() throws Exception {
        //given

        //then
        ResultActions result = mockMvc.perform(put(URI_PREFIX + "/" + member.getMemberNo())
                .content(objectMapper.writeValueAsString(updateDto))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk());
        then(memberService).should(times(1)).updateMember(anyLong(), any());
    }

    @Test
    @DisplayName("회원 수정 실패 테스트")
    void testUpdateMember_invalidFieldTest() throws Exception {
        //given
        MemberUpdateRequest blankDto = new MemberUpdateRequest();

        //when
        ResultActions result = mockMvc.perform(put(URI_PREFIX + "/" + member.getMemberNo())
                .content(objectMapper.writeValueAsString(blankDto))
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$['message']").value("Invalid parameter included"));

    }

//    @Test
    @DisplayName("회원 삭제 성공 테스트")
    void testDeleteMember_successTest() throws Exception {
        mockMvc.perform(delete(URI_PREFIX + "/" + member.getMemberNo()))
                .andExpect(status().isAccepted())
                .andDo(print())
                .andReturn();

        verify(memberService, times(1)).deleteMember(member.getMemberNo());
    }

//    @Test
    @DisplayName("회원 삭제 실패 테스트")
    void testDeleteMember_failedTest() throws Exception {
        doThrow(MemberNotFoundException.class).when(memberService)
                .deleteMember(member.getMemberNo());

        mockMvc.perform(delete(URI_PREFIX + "/" + member.getMemberNo()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }


}