package com.nhnacademy.booklay.server.controller.order;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderReceipt;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.payment.StorageRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.exception.service.NotEnoughStockException;
import com.nhnacademy.booklay.server.service.category.CategoryProductService;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import com.nhnacademy.booklay.server.service.order.ComplexOrderService;
import com.nhnacademy.booklay.server.service.order.OrderService;
import com.nhnacademy.booklay.server.service.order.RedisOrderService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.util.HashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.test.TestUtils;
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
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author 양승아
 */
@Slf4j
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com")
@ExtendWith(RestDocumentationExtension.class)
@WebMvcTest(OrderController.class)
@MockBean(JpaMetamodelMappingContext.class)
class OrderControllerTest {
    @MockBean
    ProductService productService;
    @MockBean
    RedisOrderService redisOrderService;
    @MockBean
    CategoryProductService categoryProductService;
    @MockBean
    ComplexOrderService complexOrderService;
    @MockBean
    PointHistoryService pointHistoryService;
    @MockBean
    OrderService orderService;
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    Member member;
    private static final String IDENTIFIER = "order";

    private static final String URI_PREFIX = "/" + IDENTIFIER;
    OrderReceipt orderReceipt;
    OrderListRetrieveResponse orderListRetrieveResponse;
    OrderSheet orderSheet;
    Order order;
    StorageRequest storageRequest;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) throws Exception {
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
        orderReceipt = Dummy.getDummyOrderReceipt();
        orderListRetrieveResponse = Dummy.getDummyOrderListRetrieveResponse();
        orderSheet = new OrderSheet();
        TestUtils.setFieldValue(orderSheet, "orderNo", 1L);
        TestUtils.setFieldValue(orderSheet, "orderId", "test");
        order = Dummy.getDummyOrder();
        storageRequest = Dummy.getDummyStorageRequest();
    }

    @Test
    @DisplayName("Product Data 조회 성공 테스트")
    void testGetProductDataByProductList() throws Exception {
        //given
        given(categoryProductService.retrieveCategoryIdListMultiValueMapByProductIdList(
            any())).willReturn(
            new MultiValueMapAdapter<>(new HashMap<>()));
        given(productService.retrieveProductListByProductNoList(any())).willReturn(List.of());
        List<Long> productNoList = List.of(1L);

        //when
        ResultActions result = mockMvc.perform(get(URI_PREFIX + "/products/")
            .param("productNoList", "1,2,3")
            .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        then(categoryProductService).should(times(1))
            .retrieveCategoryIdListMultiValueMapByProductIdList(any());
        then(productService).should(times(1)).retrieveProductListByProductNoList(any());
    }

    @Test
    @DisplayName("orderSheet 저장 성공 테스트")
    void testSaveOrderSheet() throws Exception {
        //given
        given(complexOrderService.checkOrder(any(), any())).willReturn(orderSheet);
        given(redisOrderService.saveOrderSheet(any())).willReturn("test");

        //when
        mockMvc.perform(post(URI_PREFIX + "/check/")
                .content(objectMapper.writeValueAsString(orderSheet))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(complexOrderService).should(times(1)).checkOrder(any(), any());
        then(redisOrderService).should(times(1)).saveOrderSheet(any());
    }

    @Test
    @DisplayName("Order Sheet 조회 성공 테스트")
    void testRetrieveOrderSheet() throws Exception {
        //mocking
        when(redisOrderService.retrieveOrderSheet(orderSheet.getOrderId())).thenReturn(orderSheet);
        //then
        mockMvc.perform(
                get(URI_PREFIX + "/sheet/" + orderSheet.getOrderId())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andDo(print()).andReturn();

        Mockito.verify(redisOrderService).retrieveOrderSheet(any());

    }

    @Test
    @DisplayName("재고 감소 테스트")
    void testProductStorageDown() throws Exception {
        //mocking
        when(productService.storageSoldOutChecker(any())).thenThrow(NotEnoughStockException.class);

        //then
        mockMvc.perform(
            post(URI_PREFIX + "/storage/down").accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storageRequest))
                .contentType(MediaType.APPLICATION_JSON));

        then(productService).should(times(1)).storageSoldOutChecker(any());

    }

    @Test
    @DisplayName("재고 증가 테스트")
    void testProductStorageUp() throws Exception {
        //mocking
        when(productService.storageRefund(any())).thenThrow(NotEnoughStockException.class);

        //then
        mockMvc.perform(
            post(URI_PREFIX + "/storage/up").accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storageRequest))
                .contentType(MediaType.APPLICATION_JSON));

        then(productService).should(times(1)).storageRefund(any());
    }

    @Test
    @DisplayName("주문정보 가져와서 영수증 저장 성공 테스트")
    void testSaveOrderReceipt() throws Exception {
        //given
        given(redisOrderService.retrieveOrderSheet(any())).willReturn(orderSheet);

        //when
        mockMvc.perform(post(URI_PREFIX + "/receipt/" + orderSheet.getOrderId())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(redisOrderService).should(times(1)).retrieveOrderSheet(any());
    }

    @Test
    @DisplayName("주문정보 가져와서 영수증 저장 실패 테스트")
    void testSaveOrderReceiptFail() throws Exception {
        //given
        TestUtils.setFieldValue(orderSheet, "orderNo", null);
        given(redisOrderService.retrieveOrderSheet(any())).willReturn(orderSheet);
        given(complexOrderService.saveReceipt(any(), any())).willReturn(order);

        //when
        mockMvc.perform(post(URI_PREFIX + "/receipt/" + orderSheet.getOrderId())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andReturn();

        //then
        then(redisOrderService).should(times(1)).retrieveOrderSheet(any());
        then(complexOrderService).should(times(1)).saveReceipt(any(), any());
    }

    @Test
    @DisplayName("영수증 번호로 영수증 조회 성공 테스트")
    void testRetrieveOrderReceipt() throws Exception {
        //mocking
        when(complexOrderService.retrieveOrderReceipt(orderReceipt.getOrderNo(),
            member.getMemberNo())).thenReturn(orderReceipt);

        //then
        mockMvc.perform(
                get(URI_PREFIX + "/receipt/" + orderReceipt.getOrderNo())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andDo(print()).andReturn();

        Mockito.verify(complexOrderService).retrieveOrderReceipt(any(), any());

    }

    @Test
    @DisplayName("영수증 리스트 조회 성공")
    void testRetrieveOrderReceiptPage() throws Exception {
        //given
        PageRequest pageRequest = PageRequest.of(0, 10);
        PageImpl<OrderListRetrieveResponse>
            response = new PageImpl<>(List.of(orderListRetrieveResponse), pageRequest, 1);
        PageResponse<OrderListRetrieveResponse> page = new PageResponse<>(response);

        //mocking
        when(orderService.retrieveOrderListRetrieveResponsePageByMemberNoAndBlind(
            member.getMemberNo(), Boolean.FALSE, pageRequest)).thenReturn(page);

        // then
        mockMvc.perform(get(URI_PREFIX + "/receipt/list")
                .queryParam("page", "0")
                .queryParam("size", "10")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andReturn();

        Mockito.verify(orderService)
            .retrieveOrderListRetrieveResponsePageByMemberNoAndBlind(any(), any(), any());
    }

    @Test
    @DisplayName("주문 확인 성공 테스트")
    void testConfirmOrder() throws Exception {
        //mocking
        when(orderService.confirmOrder(orderReceipt.getOrderNo(), member.getMemberNo())).thenReturn(
            true);

        //then
        mockMvc.perform(
                get(URI_PREFIX + "/confirm/" + orderReceipt.getOrderNo())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andDo(print()).andReturn();

        Mockito.verify(orderService).confirmOrder(any(), any());
    }
}