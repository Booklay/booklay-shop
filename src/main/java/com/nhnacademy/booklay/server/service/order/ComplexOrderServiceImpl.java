package com.nhnacademy.booklay.server.service.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.ApiEntity;
import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponUseRequest;
import com.nhnacademy.booklay.server.dto.coupon.response.CouponRetrieveResponseFromProduct;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.order.OrderProductDto;
import com.nhnacademy.booklay.server.dto.order.payment.DeliveryDetailDto;
import com.nhnacademy.booklay.server.dto.order.payment.OrderCheckDto;
import com.nhnacademy.booklay.server.dto.order.payment.OrderReceipt;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.payment.SubscribeDto;
import com.nhnacademy.booklay.server.dto.order.response.OrderReceiptSaveResponse;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.entity.OrderProduct;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.exception.order.CouponValidatingFailException;
import com.nhnacademy.booklay.server.exception.order.NotEnoughPointException;
import com.nhnacademy.booklay.server.exception.order.OrderSheetAlreadyProcessedException;
import com.nhnacademy.booklay.server.exception.order.OrderSheetNotFoundException;
import com.nhnacademy.booklay.server.exception.order.OrderSheetValidCheckFailException;
import com.nhnacademy.booklay.server.exception.order.ProductNotFoundException;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        OrderCheckDto orderCheckDto = new OrderCheckDto();
        orderCheckDto.setOrderSheet(orderSheet);
        validatingPointUsing(orderSheet, memberInfo);
        couponDataGetAndParsing(orderCheckDto, memberInfo);
        getProductData(orderCheckDto);
        parseProductListData(orderCheckDto);
        getProductsCategoryData(orderCheckDto);
        getOrderCouponDiscountSum(orderCheckDto);
        //할인된 최종금액
        long paymentPrice = getPaymentPrice(orderCheckDto);
        if (paymentPrice != orderSheet.getPaymentAmount()) {//만약 프론트에서 넘어온 값과 같다면 계산 결과가 유효하다고 판단
            throw new OrderSheetValidCheckFailException();
        }
        updateOrderSheet(orderCheckDto, memberInfo);
        return orderSheet;
    }

    /**
     * 사용 유효 검증
     * throw NotEnoughPointException 보유량보다 사용량이 많을 경우
     */
    private void validatingPointUsing(OrderSheet orderSheet, MemberInfo memberInfo) {
        if (orderSheet.getUsingPoint() > 0) {
            int point = pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())
                .getTotalPoint();
            if (point < orderSheet.getUsingPoint()) {
                throw new NotEnoughPointException();
            }
        }
    }

    /**
     * 쿠폰 코드로 쿠폰 서버에서 조회, 쿠폰정보를 맵에 저장
     * 쿠폰 데이터를 CouponUseRequest 로 변환
     * throw CouponValidatingFailException 쿠폰 코드 유효성 확인
     */
    private void couponDataGetAndParsing(OrderCheckDto orderCheckDto, MemberInfo memberInfo) {
        if (orderCheckDto.getOrderSheet().getCouponCodeList()==null) {
            return;
        }
        MultiValueMap<String, String> multiValueMap = ControllerUtil.getMemberInfoMultiValueMap(memberInfo);
        multiValueMap.put("couponCodeList", orderCheckDto.getOrderSheet().getCouponCodeList());
        ApiEntity<List<CouponRetrieveResponseFromProduct>> apiEntity = restService.get(
            gatewayIp + "/coupon/v1/coupons/codes", multiValueMap,
            new ParameterizedTypeReference<>() {});
        //쿠폰 조회 실패
        if (apiEntity.getBody() == null || orderCheckDto.getOrderSheet().getCouponCodeList().size() != apiEntity.getBody().size()){
            throw new CouponValidatingFailException();
        }
        orderCheckDto.setCouponList(apiEntity.getBody());

    }

    /**
     * 상품정보을 가져옴
     */
    private void getProductData(OrderCheckDto orderCheckDto) {
        List<Product> productList =
            productService.retrieveProductListByProductNoList(orderCheckDto.getProductNoList());
        if (productList.size() != orderCheckDto.getProductNoList().size()) {
            throw new ProductNotFoundException();
        }
        orderCheckDto.setProductList(productList);
    }

    /**
     * 상품리스트를 맵형태로 조회하기 쉽게 변경 및 총 가격등 취합
     */
    private void parseProductListData(OrderCheckDto orderCheckDto) {
        for(Product product : ListUtils.emptyIfNull(orderCheckDto.getProductList())){
            List<CouponRetrieveResponseFromProduct> couponListUsedToProduct =
                orderCheckDto.getCouponMap().get(product.getId());
            long productPrice = product.getPrice() *
                orderCheckDto.getCartDtoMap().get(product.getId()).getCount();
            orderCheckDto.addProductTotalPrice(productPrice);
            if (couponListUsedToProduct == null) {
                orderCheckDto.getProductPriceMap().put(product.getId(), productPrice);
                orderCheckDto.addProductDiscountedTotalPrice(productPrice);
            } else {
                long discountPrice = getDiscountPrice(couponListUsedToProduct, productPrice);
                long discountedPrice = discountPrice > productPrice ? 0 : productPrice - discountPrice;

                orderCheckDto.getProductPriceMap().put(product.getId(), discountedPrice);
                orderCheckDto.addProductDiscountedTotalPrice(discountedPrice);
            }
            if (product.isPointMethod()) {
                orderCheckDto.addPointAccumulateSum(
                    orderCheckDto.getProductPriceMap().get(product.getId()) * product.getPointRate() / 100
                );
            }
        }
    }

    private Long getDiscountPrice(List<CouponRetrieveResponseFromProduct> couponListUsedToProduct,
                                  long productPrice) {
        long discountAmountSum = 0L;
        for (CouponRetrieveResponseFromProduct couponData : ListUtils.emptyIfNull(couponListUsedToProduct)){
            discountAmountSum += getDiscountAmount(couponData, productPrice);
        }
        return discountAmountSum;
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

    /**
     * 상품카테고리를 분석
     */
    private void getProductsCategoryData(OrderCheckDto orderCheckDto) {
        MultiValueMap<Long, Long> categoryListMap =
            categoryProductService.retrieveCategoryIdListMultiValueMapByProductIdList(
                orderCheckDto.getProductNoList());
        //카테고리이름으로 상품리스트를 조회가능하도록 카테고리를 소속별로 분할하여 맵에 저장
        for (long productNo : orderCheckDto.getProductNoList()){
            for (Long categoryNo : categoryListMap.get(productNo)){
                String categoryNoString = categoryNo.toString();
                if (categoryNoString.length() > 3) {
                    orderCheckDto.getProductCategoryMap().
                        add(categoryNoString.substring(0, 3), productNo);
                }
                if (categoryNoString.length() > 1) {
                    orderCheckDto.getProductCategoryMap().
                        add(categoryNoString.substring(0, 1), productNo);
                }
                orderCheckDto.getProductCategoryMap().
                    add(categoryNoString, productNo);
            }
        }
    }

    /**
     * 주문쿠폰 할인액
     */
    private void getOrderCouponDiscountSum(OrderCheckDto orderCheckDto) {
        String higherCategory = null;
        // 사용된 주문쿠폰 목록
        List<CouponRetrieveResponseFromProduct> orderCouponList = orderCheckDto.getCouponMap().get(null);
        if (orderCouponList != null) {
            if (orderCouponList.size() > 1) {
                higherCategory = getHigherCategory(orderCouponList);
            }
            for (CouponRetrieveResponseFromProduct orderCoupon : orderCouponList) {
                long categoryPriceSum = 0L;
                for(long categoryNo : ListUtils.emptyIfNull(orderCheckDto.getProductCategoryMap().
                    get(orderCoupon.getCategoryNo().toString()))){
                    categoryPriceSum += orderCheckDto.getProductPriceMap().get(categoryNo);
                }
                long categoryDiscountAmount = getDiscountAmount(orderCoupon, categoryPriceSum);
                orderCheckDto.addOrderCouponDiscountSum(Math.min(categoryDiscountAmount, categoryPriceSum));
            }
            if (higherCategory == null) { return; }
            long categoryPriceSum =
                Objects.requireNonNull(orderCheckDto.getProductCategoryMap().get(higherCategory)).
                    stream().reduce(Long::sum).orElse(0L);
            if (orderCheckDto.getOrderCouponDiscountSum() > categoryPriceSum) {
                orderCheckDto.setOrderCouponDiscountSum(categoryPriceSum);
            }
        }
    }

    /**
     * @return 두 카테고리 중 상위의 카테고리
     */
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
     * @return 결제금액
     */
    private static long getPaymentPrice(OrderCheckDto orderCheckDto) {
        long discountedTotalPrice =
            Math.max(orderCheckDto.getProductDiscountedTotalPrice() -
                orderCheckDto.getOrderCouponDiscountSum(), 0);
        //할인된 최종금액 - 사용 포인트 + 배송비 + 포장비
        return discountedTotalPrice - orderCheckDto.getOrderSheet().getUsingPoint() +
            (orderCheckDto.getProductTotalPrice() > 30000 ? 0 : 3000) +
            orderCheckDto.getOrderSheet().getGiftWrappingPrice();
    }
    private void updateOrderSheet(OrderCheckDto orderCheckDto, MemberInfo memberInfo) {
        OrderSheet orderSheet = orderCheckDto.getOrderSheet();
        Map<Long, CartDto> cartDtoMap = orderCheckDto.getCartDtoMap();
        Map<Long, Product> productListMap = new HashMap<>();
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();

        List<Product> productList = orderCheckDto.getProductList();
        productList.forEach(product -> {
            productListMap.put(product.getId(), product);
            orderProductDtoList.add(new OrderProductDto(product.getId(), product.getTitle(),
                cartDtoMap.get(product.getId()).getCount(), product.getPrice().intValue()));
        });
        //subscribe 상품 분할
        List<Long> productNoList = orderCheckDto.getProductNoList();
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
        orderSheet.setCouponUseRequest(orderCheckDto.getCouponUseRequest());
        orderSheet.setMemberNo(memberInfo.getMemberNo());
        orderSheet.setPointAccumulate(orderCheckDto.getPointAccumulateSum().intValue());
    }



    @Override
    public OrderReceiptSaveResponse payOrder(String orderId, MemberInfo memberInfo) {
        OrderSheet orderSheet = getOrderSheet(orderId);
        productService.storageSoldOutChecker(orderSheet.getCartDtoList());
        Order order = saveReceipt(orderSheet, memberInfo);
        return new OrderReceiptSaveResponse(order.getId(), orderSheet.getCartDtoList(), false);
    }

    private OrderSheet getOrderSheet(String orderId) {
        OrderSheet orderSheet = redisOrderService.retrieveOrderSheet(orderId);
        if (orderSheet == null){
            throw new OrderSheetNotFoundException();
        }
        if(orderSheet.getOrderNo() != null){
            throw new OrderSheetAlreadyProcessedException();
        }
        return orderSheet;
    }

    @Override
    public Order saveReceipt(OrderSheet orderSheet, MemberInfo memberInfo) {
        Order order = orderService.saveOrder(orderSheet);
        Map<Long, Long> productNoOrderProductMap = new HashMap<>();
        //주문 상품 저장
        for(OrderProductDto orderProductDto : ListUtils.emptyIfNull(orderSheet.getOrderProductDtoList())){
            OrderProduct orderProduct = orderProductService.saveOrderProduct(orderProductDto, order.getId());
            productNoOrderProductMap.put(orderProduct.getProductNo(), orderProduct.getId());
        }
        //일반 상품용 배송정보
        if (!orderSheet.getCartDtoList().isEmpty()) {
            deliveryDetailService.saveDeliveryDetailFromOrderSheet(orderSheet, order.getId());
        }
        //구독 목록 저장
        for(SubscribeDto subscribeDto : ListUtils.emptyIfNull(orderSheet.getSubscribeProductList())){
            orderSubscribeService.saveOrderSubscribe(subscribeDto, order.getId());
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
            couponUseRequest.getProductCouponList().forEach(couponUsingDto -> couponUsingDto.setUsedTargetNo(productNoOrderProductMap.get(couponUsingDto.getUsedTargetNo())));
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
