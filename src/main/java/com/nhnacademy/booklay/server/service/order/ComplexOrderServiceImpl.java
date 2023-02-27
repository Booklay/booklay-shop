package com.nhnacademy.booklay.server.service.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.ApiEntity;
import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponUseRequest;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponUsingDto;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponRetrieveResponseFromProduct;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.order.OrderProductDto;
import com.nhnacademy.booklay.server.dto.order.payment.DeliveryDetailDto;
import com.nhnacademy.booklay.server.dto.order.payment.OrderReceipt;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheetCheckResponse;
import com.nhnacademy.booklay.server.dto.order.payment.SubscribeDto;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.entity.OrderProduct;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.service.RestService;
import com.nhnacademy.booklay.server.service.category.CategoryProductService;
import com.nhnacademy.booklay.server.service.delivery.DeliveryDetailService;
import com.nhnacademy.booklay.server.service.delivery.DeliveryStatusCodeService;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import com.nhnacademy.booklay.server.utils.ControllerUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
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
    private final CategoryProductService categoryProductService;
    private final PointHistoryService pointHistoryService;
    private final RedisOrderService redisOrderService;
    private final RestService restService;
    private final String gatewayIp;
    private final ObjectMapper objectMapper;



    @Override
    public OrderSheet checkOrder(OrderSheet orderSheet, MemberInfo memberInfo) {
        try {
            Map<Long, CartDto> cartDtoMap = new HashMap<>();

            //point 사용 유효 검증 보유량보다 사용량이 많을 경우 튕김
            if (orderSheet.getUsingPoint() > 0) {
                int point = pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())
                    .getTotalPoint();
                if (point < orderSheet.getUsingPoint()) {
                    return new OrderSheetCheckResponse("포인트보유량보다 많은 포인트 사용되었습니다.", 4, Boolean.FALSE);
                }
            }
            AtomicReference<Long> pointAccumulateSum = new AtomicReference<>(0L); // 적립 예상 포인트

            //coupon 데이터 받아오기 및 쿠폰 데이터를 쿠폰사용전송데이터로 변환
            CouponUseRequest couponUseRequest = new CouponUseRequest();
            MultiValueMap<Long, CouponRetrieveResponseFromProduct> couponMap =
                new LinkedMultiValueMap<>();
            //쿠폰 코드 유효성 확인, 쿠폰정보를 맵에 저장
            if (!couponDataGetAndParsing(orderSheet, couponUseRequest, couponMap, memberInfo)) {
                return new OrderSheetCheckResponse("유효하지 않은 쿠폰 코드가 포함되어 있습니다.", 4,
                    Boolean.FALSE); // 유효하지 않은 쿠폰 코드가 포함되어 있음
            }


            List<Long> productNoList = orderSheet.getCartDtoList().stream().map(cartDto -> {
                cartDtoMap.put(cartDto.getProductNo(), cartDto);
                return cartDto.getProductNo();
            }).collect(Collectors.toList());
            Map<Long, Long> productMap = new HashMap<>();
            List<Product> productList =
                productService.retrieveProductListByProductNoList(productNoList);
            if (productList.size() != productNoList.size()) {
                return new OrderSheetCheckResponse("확인할 수 없는 상품이 포함되어 있습니다.", 4, Boolean.FALSE);
            }
            AtomicReference<Long> productDiscountedTotalPrice =
                new AtomicReference<>(0L); // 상품쿠폰이 적용된 상품가격 합계
            AtomicReference<Long> productTotalPrice = new AtomicReference<>(0L); // 상품 가격합계

            //상품리스트를 맵형태로 조회하기 쉽게 변경 및 총 가격등 취합
            parseProductListData(cartDtoMap, pointAccumulateSum, couponMap, productMap, productList,
                productDiscountedTotalPrice, productTotalPrice);
            long paymentPrice;
            long discountedTotalPrice;

            MultiValueMap<String, Long> productCategoryMap = new LinkedMultiValueMap<>();
            MultiValueMap<Long, Long> categoryListMap =
                categoryProductService.retrieveCategoryIdListMultiValueMapByProductIdList(
                    productNoList);
            //카테고리이름으로 상품리스트를 조회가능하도록 카테고리를 소속별로 분할하여 맵에 저장
            productNoList.forEach(
                productNo -> categoryListMap.get(productNo).forEach(categoryNo -> {
                    String categoryNoString = categoryNo.toString();
                    if (categoryNoString.length() > 3) {
                        productCategoryMap.add(categoryNoString.substring(0, 3), productNo);
                    }
                    if (categoryNoString.length() > 1) {
                        productCategoryMap.add(categoryNoString.substring(0, 1), productNo);
                    }
                    productCategoryMap.add(categoryNoString, productNo);
                }));

            List<CouponRetrieveResponseFromProduct> orderCouponList =
                couponMap.get(null); // 사용된 상품쿠폰 목록
            long orderCouponDiscountSum = getOrderCouponDiscountSum(productMap, productCategoryMap,
                orderCouponList); // 상품쿠폰 할인액
            //할인된 최종금액
            discountedTotalPrice =
                Math.max(productDiscountedTotalPrice.get() - orderCouponDiscountSum, 0);
            //할인된 최종금액 - 사용 포인트 + 배송비 + 포장비
            paymentPrice = discountedTotalPrice - orderSheet.getUsingPoint() +
                (productTotalPrice.get() > 30000 ? 0 : 3000)
                + orderSheet.getGiftWrappingPrice();

            if (paymentPrice == orderSheet.getPaymentAmount()) {//만약 넘어온 값과같다면 계산 결과가 유효하다고 판단
                updateOrderSheet(orderSheet, cartDtoMap, productNoList, couponUseRequest,
                    productList);
                orderSheet.setMemberNo(memberInfo.getMemberNo());
                orderSheet.setPointAccumulate(pointAccumulateSum.get().intValue());
                return orderSheet;
            }
            return new OrderSheetCheckResponse("주문 정보의 유효성 검사가 실패하였습니다.", 4, Boolean.FALSE);
        }catch (Exception e){
            return new OrderSheetCheckResponse("유효성 검사중 알 수 없는 오류가 발생하였습니다.", 5, Boolean.FALSE);
        }
    }

    private void parseProductListData(Map<Long, CartDto> cartDtoMap,
                                      AtomicReference<Long> pointAccumulateSum,
                                      MultiValueMap<Long, CouponRetrieveResponseFromProduct> couponMap,
                                      Map<Long, Long> productMap, List<Product> productList,
                                      AtomicReference<Long> productDiscountedTotalPrice,
                                      AtomicReference<Long> productTotalPrice) {
        productList.forEach(product -> {
            List<CouponRetrieveResponseFromProduct> retrieveResponseFromProductList =
                couponMap.get(product.getId());
            long price = product.getPrice() * cartDtoMap.get(product.getId()).getCount();
            productTotalPrice.updateAndGet(v -> v + price);
            if (retrieveResponseFromProductList == null) {
                productMap.put(product.getId(), price);
                productDiscountedTotalPrice.updateAndGet(v -> v + price);
            } else {
                long discountPrice = retrieveResponseFromProductList.stream()
                    .map(couponRetrieveResponseFromProduct ->
                        getDiscountAmount(couponRetrieveResponseFromProduct, price))
                    .reduce(Long::sum).orElse(0L);
                Long discountedPrice = discountPrice > price ? 0 : price - discountPrice;
                productMap.put(product.getId(), discountedPrice);
                productDiscountedTotalPrice.updateAndGet(v -> v + discountedPrice);
            }
            if (product.isPointMethod()) {
                pointAccumulateSum.updateAndGet(
                    v -> v + (productDiscountedTotalPrice.get() * product.getPointRate() / 100));
            }
        });
    }

    private Long getDiscountAmount(CouponRetrieveResponseFromProduct coupon, Long price) {
        if (coupon.getTypeName().equals("정률쿠폰")) {
            long amount = coupon.getAmount() * price / 100;
            return amount > coupon.getMaximumDiscountAmount()?coupon.getMaximumDiscountAmount():amount;
        } else if (coupon.getTypeName().equals("정액쿠폰")) {
            return Integer.toUnsignedLong(coupon.getAmount());
        }
        return 0L;
    }

    private void updateOrderSheet(OrderSheet orderSheet, Map<Long, CartDto> cartDtoMap,
                                  List<Long> productNoList, CouponUseRequest couponUseRequest,
                                  List<Product> productList) {
        //product Map 생성/주문상품 정보 생성
        Map<Long, Product> productListMap = new HashMap<>();
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        productList.forEach(product -> {
            productListMap.put(product.getId(), product);
            orderProductDtoList.add(new OrderProductDto(product.getId(), product.getTitle(),
                cartDtoMap.get(product.getId()).getCount(), product.getPrice().intValue()));
        });
        //subscribe 상품 분할
        List<Subscribe> subscribeList =
            subscribeService.retrieveSubscribeListByProductNoList(productNoList);
        List<SubscribeDto> subscribeCartDtoList = new ArrayList<>();
        subscribeList.forEach(subscribe -> {
            if (productListMap.containsKey(subscribe.getProductNo())) {
                subscribeCartDtoList.add(new SubscribeDto(subscribe.getId(),
                    cartDtoMap.get(subscribe.getProductNo()).getCount(),
                    productListMap.get(subscribe.getProductNo()).getPrice()));
                orderSheet.getCartDtoList().remove(cartDtoMap.get(subscribe.getProductNo()));
            }
        });
        //저장
        orderSheet.setSubscribeProductList(subscribeCartDtoList);
        orderSheet.setOrderProductDtoList(orderProductDtoList);
        orderSheet.setCouponUseRequest(couponUseRequest);
    }

    private long getOrderCouponDiscountSum(Map<Long, Long> productMap,
                                           MultiValueMap<String, Long> productCategoryMap,
                                           List<CouponRetrieveResponseFromProduct> orderCouponList) {
        long orderCouponDiscountSum = 0;
        String higherCategory = null;
        if (orderCouponList != null) {
            if (orderCouponList.size() > 1) {
                higherCategory = getHigherCategory(orderCouponList);
            }
            for (CouponRetrieveResponseFromProduct orderCoupon : orderCouponList) {
                Long categoryPriceSum = Objects.requireNonNull(
                        productCategoryMap.get(orderCoupon.getCategoryNo().toString())).
                    stream().map(productMap::get).reduce(Long::sum).orElse(0L);
                Long categoryDiscountAmount = getDiscountAmount(orderCoupon, categoryPriceSum);
                orderCouponDiscountSum +=
                    categoryDiscountAmount > categoryPriceSum ? categoryPriceSum :
                        categoryDiscountAmount;
            }
            if (higherCategory != null) {
                long categoryPriceSum =
                    Objects.requireNonNull(productCategoryMap.get(higherCategory)).
                        stream().reduce(Long::sum).orElse(0L);
                if (orderCouponDiscountSum > categoryPriceSum) {
                    orderCouponDiscountSum = categoryPriceSum;
                }
            }
        }
        return orderCouponDiscountSum;
    }
    private String getHigherCategory(List<CouponRetrieveResponseFromProduct> orderCouponList) {
        int category1 = orderCouponList.get(0).getCategoryNo().toString().length();
        int category2 = orderCouponList.get(1).getCategoryNo().toString().length();
        if (category1 == category2){
            if(Objects.equals(orderCouponList.get(0).getCategoryNo(), orderCouponList.get(1).getCategoryNo())){
                return orderCouponList.get(0).getCategoryNo().toString();
            }
        }else {
            return category1>category2?
                    orderCouponList.get(1).getCategoryNo().toString():
                    orderCouponList.get(0).getCategoryNo().toString();
        }
        return null;
    }
    /**
     * 쿠폰 코드로 쿠폰 서버에서 조회해옴
     * @return true 유효한 쿠폰 코드임 / false 유효하지 않은 쿠폰코드가 포함
     */
    private boolean couponDataGetAndParsing(OrderSheet orderSheet, CouponUseRequest couponUseRequest, MultiValueMap<Long, CouponRetrieveResponseFromProduct> couponMap, MemberInfo memberInfo) {
        if (orderSheet.getCouponCodeList()!=null){
            MultiValueMap<String, String> multiValueMap = ControllerUtil.getMemberInfoMultiValueMap(memberInfo);
            multiValueMap.put("couponCodeList", orderSheet.getCouponCodeList());
            ApiEntity<List<CouponRetrieveResponseFromProduct>> apiEntity = restService.get(
                gatewayIp + "/coupon/v1/coupons/codes", multiValueMap,
                new ParameterizedTypeReference<>() {});
            //쿠폰 조회 실패
            if (apiEntity.getBody() == null || orderSheet.getCouponCodeList().size() != apiEntity.getBody().size()){
                return false;
            }
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
        return true;
    }


    @Override
    public Order saveReceipt(OrderSheet orderSheet, MemberInfo memberInfo) {
        Order order = orderService.saveOrder(orderSheet);
        //주문 상품 저장
        if (!orderSheet.getOrderProductDtoList().isEmpty()) {
            orderSheet.getOrderProductDtoList().forEach(orderProductDto ->
                orderProductService.saveOrderProduct(orderProductDto, order.getId())
            );
        }
        //일반 상품용 배송정보
        if (!orderSheet.getCartDtoList().isEmpty()) {
            deliveryDetailService.saveDeliveryDetailFromOrderSheet(orderSheet, order.getId());
        }
        //구독 목록 저장
        if (orderSheet.getSubscribeProductList() != null){
            orderSheet.getSubscribeProductList().forEach(subscribeDto ->
                orderSubscribeService.saveOrderSubscribe(subscribeDto, order.getId())
            );
        }
        //포인트 사용
        if (orderSheet.getUsingPoint()>0){
            pointHistoryService.createPointHistory(new PointHistoryCreateRequest(
                    order.getMemberNo(), -orderSheet.getUsingPoint().intValue(), "포인트 사용(주문 할인)"));
        }
        //쿠폰 사용전송
        String couponUsingUrl = gatewayIp + "/coupon/v1/coupons/using";
        CouponUseRequest couponUseRequest = orderSheet.getCouponUseRequest();
        if (!couponUseRequest.getCategoryCouponList().isEmpty() ||
                !couponUseRequest.getProductCouponList().isEmpty()){
            couponUseRequest.getCategoryCouponList().forEach(couponUsingDto -> couponUsingDto.setUsedTargetNo(order.getId()));
            restService.post(couponUsingUrl, objectMapper.convertValue(orderSheet.getCouponUseRequest(), Map.class), void.class);
        }
        orderSheet.setOrderNo(order.getId());
        redisOrderService.saveOrderSheet(orderSheet);
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
