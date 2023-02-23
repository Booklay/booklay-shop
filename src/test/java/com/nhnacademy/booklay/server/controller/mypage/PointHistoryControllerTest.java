package com.nhnacademy.booklay.server.controller.mypage;

import static org.mockito.ArgumentMatchers.any;
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
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.member.request.PointPresentRequest;
import com.nhnacademy.booklay.server.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.PointHistory;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
@WebMvcTest(PointHistoryController.class)
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class PointHistoryControllerTest {
    @MockBean
    PointHistoryService pointHistoryService;

    @Autowired
    PointHistoryController pointHistoryController;

    @Autowired
    MockMvc mockMvc;
    private static final String IDENTIFIER = "point";
    private static final String URI_PREFIX = "/" + IDENTIFIER;
    ObjectMapper objectMapper;
    Member member;
    PointHistory pointHistory;
    PointHistoryCreateRequest pointHistoryCreateRequest;
    TotalPointRetrieveResponse totalPointRetrieveResponse;
    PointPresentRequest pointPresentRequest;
    PointHistoryRetrieveResponse pointHistoryRetrieveResponse;
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

        member = Dummy.getDummyMember();
        pointHistory = Dummy.getDummyPointHistory();
        pointHistoryCreateRequest = Dummy.getDummyPointHistoryCreateRequest();
        totalPointRetrieveResponse = Dummy.getDummyTotalPointRetrieveResponse();
        pointPresentRequest = Dummy.getDummyPointPresentRequest();
        pointHistoryRetrieveResponse = Dummy.getDummyPointHistoryRetrieveResponse();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("pointHistory 등록 성공 테스트")
    void testCreatePointHistory() throws Exception {
        //given

        //when
        mockMvc.perform(post(URI_PREFIX)
                .content(objectMapper.writeValueAsString(pointHistoryCreateRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(pointHistoryService).should(times(1)).createPointHistory(any());
    }

    @Test
    @DisplayName("포인트 적립/사용 내역 조회 검색 성공")
    void testRetrievePointHistory() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<PointHistoryRetrieveResponse>
            response = new PageImpl<>(List.of(pointHistoryRetrieveResponse), pageRequest, 1);

        //mocking
        when(pointHistoryService.retrievePointHistorys(pointHistoryRetrieveResponse.getMember(),
            pageRequest)).thenReturn(response);

        // then
        mockMvc.perform(get(URI_PREFIX + "/" + pointHistoryRetrieveResponse.getMember())
                .queryParam("page", "0")
                .queryParam("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(pointHistoryService).retrievePointHistorys(any(), any());
    }

    @Test
    @DisplayName("누적포인트 조회 성공 테스트")
    void testRetrieveTotalPoint() throws Exception {
        //given
        given(pointHistoryService.retrieveTotalPoint(any())).willReturn(totalPointRetrieveResponse);

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/total/" + totalPointRetrieveResponse.getMember())
            .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                jsonPath("$['totalPoint']").value(totalPointRetrieveResponse.getTotalPoint()));

        then(pointHistoryService).should(times(1)).retrieveTotalPoint(any());
    }

    @Test
    @DisplayName("포인트 선물 성공 테스트")
    void testPresentPoint() throws Exception {
        //given

        //when
        mockMvc.perform(post(URI_PREFIX + "/present/" + member.getMemberNo())
                .content(objectMapper.writeValueAsString(pointPresentRequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(pointHistoryService).should(times(1)).presentPoint(any(), any());
    }
}