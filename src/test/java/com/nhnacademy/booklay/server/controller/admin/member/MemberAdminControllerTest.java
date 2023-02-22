package com.nhnacademy.booklay.server.controller.admin.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.member.request.MemberAuthorityUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberBlockRequest;
import com.nhnacademy.booklay.server.dto.member.response.BlockedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.DroppedMemberRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberChartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeChartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.service.member.MemberService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author 양승아
 */
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(MemberAdminController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MemberAdminControllerTest {
    @MockBean
    MemberService memberService;

    @Autowired
    MemberAdminController memberAdminController;

    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    Member member;
    MemberRetrieveResponse memberRetrieveResponse;
    MemberChartRetrieveResponse memberChartRetrieveResponse;
    MemberGradeChartRetrieveResponse memberGradeChartRetrieveResponse;
    MemberBlockRequest memberBlockRequest;
    MemberAuthorityUpdateRequest memberAuthorityUpdateRequest;
    MemberGradeRetrieveResponse memberGradeRetrieveResponse;
    BlockedMemberRetrieveResponse blockedMemberRetrieveResponse;
    DroppedMemberRetrieveResponse droppedMemberRetrieveResponse;
    private static final String IDENTIFIER = "admin/members";

    private static final String URI_PREFIX = "/" + IDENTIFIER;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(print())
            .alwaysDo(document("admin/member/{methodName}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
                )
            )
            .build();

        objectMapper = new ObjectMapper();
        member = Dummy.getDummyMember();

        memberRetrieveResponse =
            new MemberRetrieveResponse(member.getMemberNo(), member.getGender().getName(),
                member.getMemberId(), member.getNickname(), member.getName(), member.getBirthday(),
                member.getPhoneNo(), member.getEmail(), member.getCreatedAt(),
                member.getUpdatedAt(), member.getDeletedAt(), member.getIsBlocked());
        memberChartRetrieveResponse = Dummy.getDummyMemberChartRetrieveResponse();
        memberGradeChartRetrieveResponse = Dummy.getDummyMemberGradeChartRetrieveResponse();
        memberBlockRequest = Dummy.getDummyMemberBlockRequest();
        memberAuthorityUpdateRequest = Dummy.getDummyMemberAuthorityUpdateRequest();
        memberGradeRetrieveResponse = Dummy.getDummyMemberGradeRetrieveResponse();
        blockedMemberRetrieveResponse = Dummy.getDummyBlockedMemberRetrieveResponse();
        droppedMemberRetrieveResponse = Dummy.getDummyDroppedMemberRetrieveResponse();
    }

    @Test
    @DisplayName("관리자의 회원리스트 검색 테스트")
    void testRetrieveMembersSuccess() throws Exception {
        //given
        PageImpl<MemberRetrieveResponse> page = new PageImpl<>(List.of());

        //mocking
        when(memberService.retrieveMembers(any())).thenReturn(page);

        //then
        mockMvc.perform(get(URI_PREFIX))
            .andExpect(status().isOk()).andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("관리자의 단일회원 조회 성공 테스트")
    void testRetrieveMemberSuccess() throws Exception {
        //mocking
        when(memberService.retrieveMember(member.getMemberNo())).thenReturn(memberRetrieveResponse);

        //then
        mockMvc.perform(
                get(URI_PREFIX + "/" + member.getMemberNo()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andDo(print()).andReturn();
    }


    @Disabled
    @Test
    @DisplayName("관리자의 단일회원 조회 실패 테스트")
    void testRetrieveMemberTest_ifNotExistMemberId() throws Exception {
        //mocking
        when(memberService.retrieveMember(member.getMemberNo())).thenThrow(
            MemberNotFoundException.class);

        //then
        mockMvc.perform(
                get(URI_PREFIX + "/" + member.getMemberNo()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest()).andDo(print()).andReturn();
    }

    @Test
    @DisplayName("회원권한 수정 성공 테스트")
    void testUpdateMemberAuthority() throws Exception {
        //given

        //when
        mockMvc.perform(post(URI_PREFIX + "/authority/" + member.getMemberNo())
                .content(objectMapper.writeValueAsString(memberAuthorityUpdateRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(memberService).should(times(1)).createMemberAuthority(any(), any());
    }

    @Test
    @DisplayName("회원 권한 삭제 성공 테스트")
    void testDeleteMemberAuthoritySuccess() throws Exception {
        //given

        //when
        mockMvc.perform(
                delete(URI_PREFIX + "/authority/" + member.getMemberNo() + "/ROLE_USER")
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(memberService).should(times(1)).deleteMemberAuthority(any(), any());
    }

    @Test
    @DisplayName("회원 한 명의 등급 이력 조회 검색 성공")
    void testRetrieveMemberGrades() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0,10);
        PageImpl<MemberGradeRetrieveResponse> response = new PageImpl<>(List.of(memberGradeRetrieveResponse), pageRequest, 1);

        //mocking
        when(memberService.retrieveMemberGrades(member.getMemberNo(), pageRequest)).thenReturn(response);

        // then
        mockMvc.perform(get(URI_PREFIX + "/grade/" + member.getMemberNo())
                .queryParam("page", "0")
                .queryParam("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(memberService).retrieveMemberGrades(any(), any());
    }

    @Test
    @DisplayName("차단회원 리스트 조회 검색 성공")
    void testRetrieveBlockedMembers() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0,10);
        PageImpl<BlockedMemberRetrieveResponse> response = new PageImpl<>(List.of(blockedMemberRetrieveResponse), pageRequest, 1);

        //mocking
        when(memberService.retrieveBlockedMember(pageRequest)).thenReturn(response);

        // then
        mockMvc.perform(get(URI_PREFIX + "/block")
                .queryParam("page", "0")
                .queryParam("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(memberService).retrieveBlockedMember(any());
    }

    @Test
    @DisplayName("탈퇴 회원 리스트 조회 검색 성공")
    void testRetrieveDroppedMembers() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<DroppedMemberRetrieveResponse> response =
            new PageImpl<>(List.of(droppedMemberRetrieveResponse), pageRequest, 1);

        //mocking
        when(memberService.retrieveDroppedMembers(pageRequest)).thenReturn(response);

        // then
        mockMvc.perform(get(URI_PREFIX + "/dropped")
                .queryParam("page", "0")
                .queryParam("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(memberService).retrieveDroppedMembers(any());
    }

    @Test
    @DisplayName("특정 회원 차단 이력 조회 검색 성공")
    void testRetrieveBlockedMemberDetail() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<BlockedMemberRetrieveResponse> response =
            new PageImpl<>(List.of(blockedMemberRetrieveResponse), pageRequest, 1);

        //mocking
        when(memberService.retrieveBlockedMemberDetail(blockedMemberRetrieveResponse.getMemberNo(),
            pageRequest)).thenReturn(response);

        // then
        mockMvc.perform(
                get(URI_PREFIX + "/block/detail/" + blockedMemberRetrieveResponse.getMemberNo())
                    .queryParam("page", "0")
                .queryParam("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(memberService).retrieveBlockedMemberDetail(any(), any());
    }
    @Test
    @DisplayName("회원 차트 조회 성공 테스트")
    void testRetrieveMemberChart() throws Exception {
        //given

        //when
        mockMvc.perform(get(URI_PREFIX + "/chart")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(memberService).should(times(1)).retrieveMemberChart();
    }

    @Test
    @DisplayName("회원 등급 차트 조회 성공 테스트")
    void testRetrieveMemberGradeChart() throws Exception {
        //given

        //when
        mockMvc.perform(get(URI_PREFIX + "/grade/chart")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(memberService).should(times(1)).retrieveMemberGradeChart();
    }

    @Test
    @DisplayName("MemberGrade 생성 성공 테스트")
    void testCreateMemberGrade() throws Exception {
        //given

        //when
        mockMvc.perform(post(URI_PREFIX + "/grade/" + member.getMemberNo() + "/화이트")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(memberService).should(times(1)).createMemberGrade(any(),any());
    }

    @Test
    @DisplayName("차단회원 등록 성공 테스트")
    void testCreateMemberBlock() throws Exception {
        //given

        //when
        mockMvc.perform(post(URI_PREFIX + "/block/" + member.getMemberNo())
                .content(objectMapper.writeValueAsString(memberBlockRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(memberService).should(times(1)).createBlockMember(any(),any());
    }

    @Test
    @DisplayName("차단회원 해제 성공 테스트")
    void testMemberBlockCancel() throws Exception {
        //given

        //when
        mockMvc.perform(post(URI_PREFIX + "/block/cancel/" + member.getMemberNo())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(memberService).should(times(1)).blockMemberCancel(any());
    }
}
