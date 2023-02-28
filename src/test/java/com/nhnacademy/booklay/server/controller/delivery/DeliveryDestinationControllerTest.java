package com.nhnacademy.booklay.server.controller.delivery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.delivery.request.DeliveryDestinationCURequest;
import com.nhnacademy.booklay.server.dto.delivery.response.DeliveryDestinationRetrieveResponse;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.DeliveryDestination;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.exception.delivery.DeliveryDestinationLimitExceededException;
import com.nhnacademy.booklay.server.exception.delivery.DeliveryDestinationNotFoundException;
import com.nhnacademy.booklay.server.service.delivery.DeliveryDestinationService;
import java.util.List;
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
@WebMvcTest(DeliveryDestinationController.class)
@ExtendWith(RestDocumentationExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
class DeliveryDestinationControllerTest {
    @MockBean
    DeliveryDestinationService deliveryDestinationService;

    @Autowired
    DeliveryDestinationController deliveryDestinationController;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper;
    Member member;
    DeliveryDestination deliveryDestination;
    DeliveryDestinationCURequest deliveryDestinationCURequest;
    DeliveryDestinationRetrieveResponse deliveryDestinationRetrieveResponse;

    private static final String IDENTIFIER = "delivery/destination";
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

        member = Dummy.getDummyMember();
        deliveryDestination = Dummy.getDummyDeliveryDestination();
        deliveryDestinationCURequest = Dummy.getDummyDeliveryDestinationCreateRequest();
        deliveryDestinationRetrieveResponse = Dummy.getDummyDeliveryDestinationRetrieveResponse();

        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("배송지 등록 성공 테스트")
    void testCreateDeliveryDestination() throws Exception {
        //given

        //when
        mockMvc.perform(post(URI_PREFIX + "/" + member.getMemberNo())
                .content(objectMapper.writeValueAsString(deliveryDestinationCURequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(deliveryDestinationService).should(times(1)).createDeliveryDestination(any(), any());
    }

    @Test
    @DisplayName("배송지 등록 시 갯수 초과")
    void testCreateDeliveryDestination_throwsDeliveryDestinationLimitExceeded() throws Exception {
        //given
        doThrow(DeliveryDestinationLimitExceededException.class).when(deliveryDestinationService).createDeliveryDestination(any(), any());

        //when
        mockMvc.perform(post(URI_PREFIX + "/" + member.getMemberNo())
                .content(objectMapper.writeValueAsString(deliveryDestinationCURequest))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andReturn();

        //then
        then(deliveryDestinationService).should(times(1)).createDeliveryDestination(any(), any());
    }

    @Test
    @DisplayName("배송지 리스트 조회 성공 테스트")
    void testRetrieveDeliveryDestinations() throws Exception {
        //given
        given(deliveryDestinationService.retrieveDeliveryDestinations(any())).willReturn(
            List.of(deliveryDestinationRetrieveResponse));

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/list/" + member.getMemberNo())
            .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[*].['zipCode']").value(
                deliveryDestinationRetrieveResponse.getZipCode()));

        then(deliveryDestinationService).should(times(1)).retrieveDeliveryDestinations(any());
    }

    @Test
    @DisplayName("배송지 단건 조회 성공 테스트")
    void testRetrieveDeliveryDestination() throws Exception {
        //given
        given(deliveryDestinationService.retrieveDeliveryDestination(any())).willReturn(
            deliveryDestinationRetrieveResponse);

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/" + deliveryDestination.getId())
            .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                jsonPath("$['zipCode']").value(deliveryDestinationRetrieveResponse.getZipCode()));

        then(deliveryDestinationService).should(times(1)).retrieveDeliveryDestination(any());
    }

    @Test
    @DisplayName("배송지 수정 성공 테스트")
    void testUpdateDeliveryDestination() throws Exception {
        //given

        //when
        mockMvc.perform(
                post(URI_PREFIX + "/update/" + member.getMemberNo() + "/" + deliveryDestination.getId())
                    .content(objectMapper.writeValueAsString(deliveryDestinationCURequest))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(deliveryDestinationService).should(times(1))
            .updateDeliveryDestination(any(), any(), any());
    }

    @Test
    @DisplayName("배송지 수정 시 배송지 찾을 수 없음.")
    void testUpdateDeliveryDestination_throwDeliveryDestinationNotFound() throws Exception {
        //given
        doThrow(DeliveryDestinationNotFoundException.class).when(deliveryDestinationService).updateDeliveryDestination(any(), any(), any());

        //when
        mockMvc.perform(
                post(URI_PREFIX + "/update/" + member.getMemberNo() + "/" + deliveryDestination.getId())
                    .content(objectMapper.writeValueAsString(deliveryDestinationCURequest))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print())
            .andReturn();

        //then
        then(deliveryDestinationService).should(times(1))
            .updateDeliveryDestination(any(), any(), any());
    }

    @Test
    @DisplayName("배송지 삭제 성공 테스트")
    void testDeleteDeliveryDestination() throws Exception {
        //given

        //when
        mockMvc.perform(
                delete(URI_PREFIX + "/" + member.getMemberNo() + "/" + deliveryDestination.getId())
                    .content(objectMapper.writeValueAsString(deliveryDestinationCURequest))
                    .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(deliveryDestinationService).should(times(1)).deleteDeliveryDestination(any(), any());
    }
}