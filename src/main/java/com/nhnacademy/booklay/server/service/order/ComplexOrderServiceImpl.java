package com.nhnacademy.booklay.server.service.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.ApiEntity;
import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponUseRequest;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponUsingDto;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponRetrieveResponseFromProduct;
import com.nhnacademy.booklay.server.dto.order.DeliveryDetailDto;
import com.nhnacademy.booklay.server.dto.order.OrderProductDto;
import com.nhnacademy.booklay.server.dto.order.OrderReceipt;
import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.SubscribeDto;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.entity.OrderProduct;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.service.RestService;
import com.nhnacademy.booklay.server.service.delivery.DeliveryDetailService;
import com.nhnacademy.booklay.server.service.delivery.DeliveryStatusCodeService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplexOrderServiceImpl implements ComplexOrderService{
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;
    private final DeliveryDetailService deliveryDetailService;
    private final DeliveryStatusCodeService deliveryStatusCodeService;
    private final ProductService productService;
    private final SubscribeService subscribeService;
    private final OrderSubscribeService orderSubscribeService;
    private final OrderProductService orderProductService;
    private final RestService restService;
    private final String gatewayIp;
    private final ObjectMapper objectMapper;
    @Override
    public OrderSheet checkOrder(OrderSheet orderSheet, MemberInfo memberInfo) {
        Map<Long, CartDto> cartDtoMap = new HashMap<>();
        List<Long> productNoList = orderSheet.getCartDtoList().stream().map(cartDto -> {
            cartDtoMap.put(cartDto.getProductNo(), cartDto);
            return cartDto.getProductNo();
        }).collect(Collectors.toList());


        //coupon 데이터 받아오기 및 쿠폰 데이터를 쿠폰사용전송데이터로 변환
        CouponUseRequest couponUseRequest = new CouponUseRequest();
        MultiValueMap<Long, CouponRetrieveResponseFromProduct> couponMap = new LinkedMultiValueMap<>();
        if (orderSheet.getCouponCodeList()!=null){
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            multiValueMap.put("couponCodeList", orderSheet.getCouponCodeList());
            ApiEntity<List<CouponRetrieveResponseFromProduct>> apiEntity = restService.get(
                gatewayIp + "coupon/v1/coupons/codes", multiValueMap,
                new ParameterizedTypeReference<>() {});
            apiEntity.getBody().forEach(couponRetrieveResponseFromProduct -> {
                couponMap.add(couponRetrieveResponseFromProduct.getProductNo(), couponRetrieveResponseFromProduct);
                //상품쿠폰변환
                if (couponRetrieveResponseFromProduct.getProductNo()!=null){
                    couponUseRequest.getProductCouponList().add(new CouponUsingDto(
                        couponRetrieveResponseFromProduct.getCouponCode(),
                        couponRetrieveResponseFromProduct.getId(),
                        couponRetrieveResponseFromProduct.getProductNo()));
                //주문쿠폰변환
                }else {
                    couponUseRequest.getCategoryCouponList().add(new CouponUsingDto(
                        couponRetrieveResponseFromProduct.getCouponCode(),
                        couponRetrieveResponseFromProduct.getId(),
                        couponRetrieveResponseFromProduct.getCategoryNo()));
                }
            });
        }

        //todo 주문 유효 체크
        List<Product> productList = productService.retrieveProductListByProductNoList(productNoList);

        if (true){
            //product Map 생성/주문상품 정보 생성
            Map<Long, Product> productListMap = new HashMap<>();
            List<OrderProductDto> orderProductDtoList = new ArrayList<>();
            productList.forEach(product -> {
                productListMap.put(product.getId(), product);
                orderProductDtoList.add(new OrderProductDto(product.getId(), product.getTitle(), cartDtoMap.get(product.getId()).getCount(), product.getPrice().intValue()));
            });
            //subscribe 상품 분할
            List<Subscribe> subscribeList = subscribeService.retrieveSubscribeListByProductNoList(productNoList);
            List<SubscribeDto> subscribeCartDtoList = new ArrayList<>();
            subscribeList.forEach(subscribe -> {
                if (productListMap.containsKey(subscribe.getProductNo())){
                    subscribeCartDtoList.add(new SubscribeDto(subscribe.getId(), cartDtoMap.get(subscribe.getProductNo()).getCount(), productListMap.get(subscribe.getProductNo()).getPrice()));
                    orderSheet.getCartDtoList().remove(cartDtoMap.get(subscribe.getProductNo()));
                }
            });
            //저장
            orderSheet.setSubscribeProductList(subscribeCartDtoList);
            orderSheet.setOrderProductDtoList(orderProductDtoList);

            //쿠폰 사용전송
            String couponUsingUrl = gatewayIp + "/coupon/v1/coupons/using";
            restService.post(couponUsingUrl, objectMapper.convertValue(couponUseRequest, Map.class), void.class);

            return orderSheet;
        }
        return null;
    }

    @Override
    public Order saveReceipt(OrderSheet orderSheet){
        Order order = orderService.saveOrder(orderSheet);
        //주문 상품 저장
        if (!orderSheet.getOrderProductDtoList().isEmpty()){
            orderSheet.getOrderProductDtoList().forEach(orderProductDto ->
                orderProductService.saveOrderProduct(orderProductDto, order.getId())
            );
        }
        //일반 상품용 배송정보
        if (!orderSheet.getCartDtoList().isEmpty()){
            deliveryDetailService.saveDeliveryDetailFromOrderSheet(orderSheet, order.getId());
        }
        //구독 목록 저장
        if (orderSheet.getSubscribeProductList() != null){
            orderSheet.getSubscribeProductList().forEach(subscribeDto ->
                orderSubscribeService.saveOrderSubscribe(subscribeDto, order.getId())
            );
        }

        return order;
    }

    @Override
    public OrderReceipt retrieveOrderReceipt(Long orderNo, Long memberNo) {
        Order order = orderService.retrieveOrder(orderNo);
        if (order.getMemberNo()!= null && !Objects.equals(order.getMemberNo(), memberNo)){
            return new OrderReceipt();
        }
        List<OrderProduct> orderProductList = orderProductService.retrieveOrderProductListByOrderNo(orderNo);
        List<DeliveryDetail> deliveryDetailList = deliveryDetailService.retrieveDeliveryDetailByMemberNoAndOrderNo(order.getMemberNo() == null?null:memberNo, orderNo);
        OrderReceipt orderReceipt = new OrderReceipt(order);
        orderReceipt.setOrderStatus(orderStatusService.retrieveOrderStatusCodeName(orderReceipt.getOrderStatusNo()));
        orderReceipt.setOrderProductDtoList(
            orderProductList.stream().map(OrderProductDto::new).collect(Collectors.toList()));
        orderReceipt.setDeliveryDetailDtoList(
            deliveryDetailList.stream().map(deliveryDetail -> {
                DeliveryDetailDto deliveryDetailDto = new DeliveryDetailDto(deliveryDetail);
                deliveryDetailDto.setDeliveryStatus(deliveryStatusCodeService.retrieveOrderStatusCodeName(deliveryDetailDto.getDeliveryStatusNo()));
                return deliveryDetailDto;
            }).collect(Collectors.toList())
        );
        return orderReceipt;
    }
}
