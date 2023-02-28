package com.nhnacademy.booklay.server.controller.admin.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.controller.admin.member.MemberAdminController;
import com.nhnacademy.booklay.server.controller.auth.AuthController;
import com.nhnacademy.booklay.server.dto.member.response.MemberLoginResponse;
import com.nhnacademy.booklay.server.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.member.MemberNotFoundException;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.service.member.MemberService;
import java.util.Optional;
import org.apache.kafka.test.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AuthControllerTest {
    @MockBean
    MemberService memberService;
    @Autowired
    AuthController authController;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    Member member;
    MemberLoginResponse memberLoginResponse;
    private static final String IDENTIFIER = "members/login";

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
        memberLoginResponse = Dummy.getDummyMemberLoginResponse();
    }
    @Test
    @DisplayName("login 성공 테스트")
    void testDoLogin() throws Exception {
        //given
        given(memberService.retrieveMemberById(any())).willReturn(
            Optional.of(memberLoginResponse));
        TestUtils.setFieldValue(memberLoginResponse, "isBlocked", false);

        //when
        mockMvc.perform(get(URI_PREFIX)
                .param("memberId", memberLoginResponse.getUserId())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(memberService).should(times(1)).retrieveMemberById(any());
    }

    @Test
    @DisplayName("차단회원 로그인 시 실패 테스트")
    void testDoLogin_blockedMember_bad_request() throws Exception {
        //mocking
        given(memberService.retrieveMemberById(any())).willReturn(
            Optional.of(memberLoginResponse));
        TestUtils.setFieldValue(memberLoginResponse, "isBlocked", true);

        //then
        mockMvc.perform(get(URI_PREFIX + "/")
                .param("memberId", memberLoginResponse.getUserId())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();
    }

    @Test
    @DisplayName("회원의 본인정보 조회 실패 시 에러 메시지 반환 테스트")
    void testRetrieveMember_ifNotExistMemberNo_thenThrownValidMemberNotFoundErrorMessage()
        throws Exception {
        //given
        given(memberService.retrieveMemberById(any())).willThrow(
            new MemberNotFoundException("없는 회원이거나, 탈퇴한 회원입니다."));

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX)
            .param("memberId", member.getMemberId())
            .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isNotFound())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$['message']").value("Member Not Found, Member Id : 없는 회원이거나, 탈퇴한 회원입니다."));

        then(memberService).should(times(1)).retrieveMemberById(any());
    }

    @Test
    @DisplayName("login 성공 테스트")
    void testRetrieveMemberByEmail() throws Exception {
        //given
        given(memberService.retrieveMemberByEmail(any())).willReturn(
            Optional.of(memberLoginResponse));

        //when
        mockMvc.perform(get(URI_PREFIX + "/email")
                .param("email", memberLoginResponse.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(memberService).should(times(1)).retrieveMemberByEmail(any());
    }
}
