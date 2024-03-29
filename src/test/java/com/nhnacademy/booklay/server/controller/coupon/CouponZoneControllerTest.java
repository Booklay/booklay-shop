package com.nhnacademy.booklay.server.controller.coupon;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponIssueRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.service.kafka.CouponZoneIssueService;
import com.nhnacademy.booklay.server.service.member.GetMemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(CouponZoneController.class)
@MockBean(JpaMetamodelMappingContext.class)
class CouponZoneControllerTest {

    @MockBean
    GetMemberService memberService;

    @MockBean
    CouponZoneIssueService issueService;

    @Autowired
    CouponZoneController couponZoneController;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    private static final String IDENTIFIER = "member/coupon-zone";
    private static final String URI_PREFIX = "/" + IDENTIFIER;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(documentationConfiguration(restDocumentation))
            .alwaysDo(print())
            .alwaysDo(document("coupon/couponzone/{methodName}",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint())
                )
            )
            .build();
    };

    @Test
    @DisplayName("쿠폰 요청 받은 후, requestId 보내기 완료.")
    void issueCouponAtCouponZone() throws Exception {
        // given
        CouponIssueRequest request = Dummy.getDummyCouponIssueRequest();

        // when

        // then
        mockMvc.perform(post(URI_PREFIX)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();

        Mockito.verify(memberService).getMemberNo(1L);
    }

    @Test
    @DisplayName("requestId로 응답 조회 성공")
    void responseIssueCoupon() throws Exception {
        // given

        // when

        // then
        mockMvc.perform(get(URI_PREFIX + "/requestId"))
            .andExpect(status().isCreated())
            .andDo(print())
            .andReturn();

        Mockito.verify(issueService).getResponse("requestId");
    }
}