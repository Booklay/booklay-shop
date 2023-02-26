package com.nhnacademy.booklay.server.service.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.config.WebConfig;
import com.nhnacademy.booklay.server.dto.ApiEntity;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponRetrieveResponseFromProduct;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.repository.mypage.PointHistoryRepository;
import com.nhnacademy.booklay.server.service.RestService;
import com.nhnacademy.booklay.server.service.category.CategoryProductService;
import com.nhnacademy.booklay.server.service.delivery.DeliveryDetailService;
import com.nhnacademy.booklay.server.service.delivery.DeliveryStatusCodeService;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import com.nhnacademy.booklay.server.utils.ControllerUtil;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ComplexOrderServiceImplTest {

    @InjectMocks
    ComplexOrderServiceImpl complexOrderService;

    @Mock
    OrderService orderService;
    @Mock
    RestService restService;
    @Mock
    PointHistoryRepository pointHistoryRepository;
    @Mock
    OrderStatusService orderStatusService;
    @Mock
    DeliveryDetailService deliveryDetailService;
    @Mock
    DeliveryStatusCodeService deliveryStatusCodeService;
    @Mock
    ProductService productService;
    @Mock
    SubscribeService subscribeService;
    @Mock
    OrderSubscribeService orderSubscribeService;
    @Mock
    OrderProductService orderProductService;
    @Mock
    CategoryProductService categoryProductService;
    @Mock
    PointHistoryService pointHistoryService;
    @Mock
    RedisOrderService redisOrderService;

    ObjectMapper objectMapper;

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 쿠폰을 사용하지 않고, 소유한 포인트보다 적은 포인트를 사용했을 때.")
    void checkOrder_usingOnlyPoint() {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();

        // when
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
        BDDMockito.then(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo()));
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 쿠폰을 사용하지 않고, 소유한 포인트보다 적은 포인트를 사용했을 때.")
    void checkOrder_usingPointMoreThanOwnedPoint() {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        ReflectionTestUtils.setField(orderSheet, "usingPoint", 2000L);

        // when
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 유효하지 않은 쿠폰을 사용했을 때 쿠폰 조회 실패.")
    void checkOrder_usingInvalidCoupon() {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));

        // when
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(HttpStatus.BAD_REQUEST),new HttpClientErrorException(HttpStatus.BAD_REQUEST)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 쿠폰을 사용했을 때.")
    void checkOrder_usingCoupon() throws JsonProcessingException {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));

        // when
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(Dummy.getDummyCouponRetrieveResponseFromProduct(), HttpStatus.OK),new HttpClientErrorException(HttpStatus.OK)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    void saveReceipt() {
        // given

        // when

        // then

    }

    @Test
    void retrieveOrderReceipt() {
        // given

        // when

        // then

    }
}