package com.nhnacademy.booklay.server.controller.member;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.server.dto.member.response.MemberAuthorityRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberGradeRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberMainRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.service.member.MemberService;
import java.util.List;
import java.util.Optional;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author 양승아
 */
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@WebMvcTest(MemberController.class)
@ExtendWith(RestDocumentationExtension.class)
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
    MemberMainRetrieveResponse memberMainRetrieveResponse;
    MemberLoginResponse memberLoginResponse;
    MemberRetrieveResponse memberRetrieveResponse;
    MemberAuthorityRetrieveResponse memberAuthorityRetrieveResponse;
    MemberGradeRetrieveResponse memberGradeRetrieveResponse;
    private static final String IDENTIFIER = "members";

    private static final String URI_PREFIX = "/" + IDENTIFIER;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(print())
            .alwaysDo(document("{ClassName}/{methodName}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
                )
            )
            .build();

        objectMapper = new ObjectMapper();
        member = Dummy.getDummyMember();

        responseDto = new MemberRetrieveResponse(
            member.getMemberNo(),
            member.getGender().getName(),
            member.getMemberId(),
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
        memberMainRetrieveResponse = Dummy.getDummyMemberMainRetrieveResponse();
        memberLoginResponse = Dummy.getDummyMemberLoginResponse();
        memberRetrieveResponse = Dummy.getDummyMemberRetrieveResponse();
        memberAuthorityRetrieveResponse = Dummy.getDummyMemberAuthorityRetrieveResponse();
        memberGradeRetrieveResponse = Dummy.getDummyMemberGradeRetrieveResponse();
    }

    @Test
    @DisplayName("회원가입 시 아이디 중복체크 성공 테스트")
    void testExistMemberId() throws Exception {
        //given
        given(memberService.checkMemberId(any())).willReturn(true);

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/exist/" + member.getMemberId()));

        //then
        result.andExpect(status().isOk());

        then(memberService).should(times(1)).checkMemberId(any());
    }

    @Test
    @DisplayName("회원가입 시 닉네임 중복체크 성공 테스트")
    void testExistNickName() throws Exception {
        //given
        given(memberService.checkNickName(any())).willReturn(true);

        //when
        ResultActions result =
            mockMvc.perform(get(URI_PREFIX + "/exist/nickName/" + member.getNickname()));

        //then
        result.andExpect(status().isOk());


        then(memberService).should(times(1)).checkNickName(any());
    }

    @Test
    @DisplayName("회원가입 시 이메일 중복체크 성공 테스트")
    void testExistEMail() throws Exception {
        //given
        given(memberService.checkEMail(any())).willReturn(true);

        //when
        ResultActions result =
            mockMvc.perform(get(URI_PREFIX + "/exist/eMail/" + member.getEmail()));

        //then
        result.andExpect(status().isOk());


        then(memberService).should(times(1)).checkEMail(any());
    }

    @Test
    @DisplayName("회원의 개인정보 조회 성공 테스트")
    void testRetrieveMember() throws Exception {
        //given
        given(memberService.retrieveMember(anyLong())).willReturn(responseDto);

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/" + member.getMemberNo())
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$['memberId']").value(member.getMemberId()))
            .andExpect(jsonPath("$['name']").value("유재석"));

        then(memberService).should(times(1)).retrieveMember(member.getMemberNo());
    }

    @Test
    @DisplayName("회원의 MyPage main 정보 조회 성공 테스트")
    void testRetrieveMemberMain() throws Exception {
        //given
        given(memberService.retrieveMemberMain(any())).willReturn(memberMainRetrieveResponse);

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/main/" + member.getMemberNo())
            .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$['memberGrade']").value("화이트"))
            .andExpect(jsonPath("$['currentTotalPoint']").value(1000));

        then(memberService).should(times(1)).retrieveMemberMain(member.getMemberNo());
    }

    @Test
    @DisplayName("이메일로 회원 로그인 정보 조회 성공 테스트")
    void testRetrieveMemberByEmail() throws Exception {
        //given
        given(memberService.retrieveMemberByEmail(any())).willReturn(
            Optional.of(memberLoginResponse));

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/email/" + member.getEmail())
            .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$['email']").value(memberLoginResponse.getEmail()))
            .andExpect(jsonPath("$['userId']").value(memberLoginResponse.getUserId()));

        then(memberService).should(times(1)).retrieveMemberByEmail(member.getEmail());
    }

    @Test
    @DisplayName("이메일로 회원 로그인 정보 조회 성공 테스트")
    void testRetrieveMemberInfoByEmail() throws Exception {
        //given
        given(memberService.retrieveMemberInfoByEmail(any())).willReturn(
            Optional.of(memberRetrieveResponse));

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/memberinfo/" + member.getEmail())
            .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$['memberId']").value(memberRetrieveResponse.getMemberId()))
            .andExpect(jsonPath("$['nickname']").value(memberRetrieveResponse.getNickname()));

        then(memberService).should(times(1)).retrieveMemberInfoByEmail(any());
    }

    @Disabled
    @Test
    @DisplayName("회원 등급 리스트 조회 성공 테스트")
    void testRetrieveMemberGradeWithPageable() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<MemberGradeRetrieveResponse>
            response = new PageImpl<>(List.of(memberGradeRetrieveResponse), pageRequest, 1);

        //mocking
        when(memberService.retrieveMemberGrades(member.getMemberNo(), pageRequest)).thenReturn(
            response);

        // then회정
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
    @DisplayName("회원 권한 리스트 조회 성공 테스트")
    void testRetrieveMemberAuthority() throws Exception {
        //given
        given(memberService.retrieveMemberAuthority(any())).willReturn(
            List.of(memberAuthorityRetrieveResponse));

        //when
        ResultActions result =
            mockMvc.perform(get(URI_PREFIX + "/authority/" + member.getMemberNo())
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[*].['name']").value(memberAuthorityRetrieveResponse.getName()));

        then(memberService).should(times(1)).retrieveMemberAuthority(any());
    }

    @Test
    @DisplayName("회원의 본인정보 조회 실패 시 에러 메시지 반환 테스트")
    void testRetrieveMember_ifNotExistMemberNo_thenThrownValidMemberNotFoundErrorMessage()
        throws Exception {
        //given
        given(memberService.retrieveMember(anyLong())).willThrow(
            new MemberNotFoundException(member.getMemberNo()));

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
    void testUpdateMember_invalidField() throws Exception {
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

    @Test
    @DisplayName("회원 삭제 성공 테스트")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete(URI_PREFIX + "/" + member.getMemberNo()))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        verify(memberService, times(1)).deleteMember(any());
    }
}