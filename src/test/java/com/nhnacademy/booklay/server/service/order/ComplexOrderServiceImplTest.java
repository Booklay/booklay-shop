package com.nhnacademy.booklay.server.service.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.config.WebConfig;
import com.nhnacademy.booklay.server.dto.ApiEntity;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponRetrieveResponseFromProduct;
import com.nhnacademy.booklay.server.dto.member.response.TotalPointRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.payment.SubscribeDto;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
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
import java.util.Map;
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
import org.springframework.util.LinkedMultiValueMap;
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

    @Mock
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
        then(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo()));
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
    @DisplayName("주문 유효성 검사 성공 :: 상품 쿠폰을 사용했을 때.")
    void checkOrder_usingProductCoupon() throws JsonProcessingException {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        CouponRetrieveResponseFromProduct response = Dummy.getDummyCouponRetrieveResponseFromProduct_amount();
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));
        ReflectionTestUtils.setField(response, "productNo", 1L);

        // when
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(List.of(response), HttpStatus.OK),new HttpClientErrorException(HttpStatus.OK)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 주문 쿠폰을 사용했을 때.")
    void checkOrder_usingCategoryCoupon() throws JsonProcessingException {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        CouponRetrieveResponseFromProduct response = Dummy.getDummyCouponRetrieveResponseFromProduct_amount();
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));

        // when
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(List.of(response), HttpStatus.OK),new HttpClientErrorException(HttpStatus.OK)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 유효성 검사중 알 수 없는 오류 발생.")
    void checkOrder_ValidationError() throws JsonProcessingException {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));

        // when
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(Dummy.getDummyCouponRetrieveResponseFromProduct_amount(), HttpStatus.OK),new HttpClientErrorException(HttpStatus.OK)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 확인할 수 없는 상품이 포함되어 있습니다..")
    void checkOrder_validationProduct_NoProduct() throws JsonProcessingException {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        CouponRetrieveResponseFromProduct response = Dummy.getDummyCouponRetrieveResponseFromProduct_amount();
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));

        // when
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(List.of(response), HttpStatus.OK),new HttpClientErrorException(HttpStatus.OK)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 상품 유효성 검사. retrieveResponseFromProductList == null")
    void checkOrder_validationProduct_RetrieveResponseIsNull() throws JsonProcessingException {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        Product product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
        CouponRetrieveResponseFromProduct response = Dummy.getDummyCouponRetrieveResponseFromProduct_amount();
        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));

        // when
        when(productService.retrieveProductListByProductNoList(anyList())).thenReturn(List.of(product));
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(List.of(Dummy.getDummyCouponRetrieveResponseFromProduct_amount()), HttpStatus.OK),new HttpClientErrorException(HttpStatus.OK)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 상품 유효성 검사. retrieveResponseFromProductList != null")
    void checkOrder_validationProduct_RetrieveResponseIsNotNull() throws JsonProcessingException {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        Product product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
        CouponRetrieveResponseFromProduct response = Dummy.getDummyCouponRetrieveResponseFromProduct_amount();
        ReflectionTestUtils.setField(response, "productNo", 1L);
        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));

        // when
        when(productService.retrieveProductListByProductNoList(anyList())).thenReturn(List.of(product));
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(List.of(response), HttpStatus.OK),new HttpClientErrorException(HttpStatus.OK)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 상품 유효성 검사. retrieveResponseFromProductList != null. 정률쿠폰 사용")
    void checkOrder_validationProduct_RetrieveResponseIsNotNull_usingPercentCoupon() throws JsonProcessingException {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        Product product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
        CouponRetrieveResponseFromProduct response = Dummy.getDummyCouponRetrieveResponseFromProduct_percent();
        ReflectionTestUtils.setField(response, "productNo", 1L);
        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));

        // when
        when(productService.retrieveProductListByProductNoList(anyList())).thenReturn(List.of(product));
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(List.of(response), HttpStatus.OK),new HttpClientErrorException(HttpStatus.OK)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문 유효성 검사 성공 :: 상품 유효성 검사. retrieveResponseFromProductList != null. 포인트 쿠폰 사용")
    void checkOrder_validationProduct_RetrieveResponseIsNotNull_usingPointCoupon() throws JsonProcessingException {
        // given
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        Product product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
        CouponRetrieveResponseFromProduct response = Dummy.getDummyCouponRetrieveResponseFromProduct_percent();
        ReflectionTestUtils.setField(orderSheet, "paymentAmount", 17900L);
        ReflectionTestUtils.setField(response, "productNo", 1L);
        ReflectionTestUtils.setField(response, "typeName", "포인트");
        ReflectionTestUtils.setField(product, "id", 1L);
        ReflectionTestUtils.setField(orderSheet, "couponCodeList", List.of("testCouponCode"));
        Subscribe dummySubscribe =
            DummyCart.getDummySubscribe(DummyCart.getDummyProductSubscribeDto());
        ReflectionTestUtils.setField(dummySubscribe, "productNo", 1L);

        MultiValueMap<Long, Long> categoryListMap = new LinkedMultiValueMap<>();
        categoryListMap.add(1L, 1L);

        // when

        when(subscribeService.retrieveSubscribeListByProductNoList(anyList())).thenReturn(List.of(
            dummySubscribe));
        when(categoryProductService.retrieveCategoryIdListMultiValueMapByProductIdList(anyList())).thenReturn(categoryListMap);
        when(productService.retrieveProductListByProductNoList(anyList())).thenReturn(List.of(product));
        when(pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())).thenReturn(new TotalPointRetrieveResponse(memberInfo.getMemberNo(), 1000));
        when(restService.get(anyString(), (MultiValueMap<String, String>) any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(new ApiEntity<>(new ResponseEntity<>(List.of(response), HttpStatus.OK),new HttpClientErrorException(HttpStatus.OK)));

        complexOrderService.checkOrder(orderSheet, memberInfo);

        // then
    }

    @Test
    @DisplayName("주문서 저장 성공")
    void saveReceipt() {
        // given
        Order order = Dummy.getDummyOrder();
        OrderSheet orderSheet = Dummy.getDummyOrderSheet();
        MemberInfo memberInfo = Dummy.getDummyMemberInfo();
        when(orderService.saveOrder(any())).thenReturn(order);

        // when
        complexOrderService.saveReceipt(orderSheet, memberInfo);

        // then
        then(orderService).should(times(1)).saveOrder(any());
    }

    @Test
    @DisplayName("주문서 조회 성공")
    void retrieveOrderReceipt() {
        // given
        Long targetId = 1L;
        Order order = Dummy.getDummyOrder();
        when(orderService.retrieveOrder(targetId)).thenReturn(order);

        // when
        complexOrderService.retrieveOrderReceipt(targetId, targetId);

        // then
        then(orderService).should(times(1)).retrieveOrder(any());
    }

}