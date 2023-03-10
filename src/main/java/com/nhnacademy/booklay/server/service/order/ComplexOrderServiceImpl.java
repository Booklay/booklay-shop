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

            //point ?????? ?????? ?????? ??????????????? ???????????? ?????? ?????? ??????
            if (orderSheet.getUsingPoint() > 0) {
                int point = pointHistoryService.retrieveTotalPoint(memberInfo.getMemberNo())
                    .getTotalPoint();
                if (point < orderSheet.getUsingPoint()) {
                    return new OrderSheetCheckResponse("???????????????????????? ?????? ????????? ?????????????????????.", 4, Boolean.FALSE);
                }
            }
            AtomicReference<Long> pointAccumulateSum = new AtomicReference<>(0L); // ?????? ?????? ?????????

            //coupon ????????? ???????????? ??? ?????? ???????????? ?????????????????????????????? ??????
            CouponUseRequest couponUseRequest = new CouponUseRequest();
            MultiValueMap<Long, CouponRetrieveResponseFromProduct> couponMap =
                new LinkedMultiValueMap<>();
            //?????? ?????? ????????? ??????, ??????????????? ?????? ??????
            if (!couponDataGetAndParsing(orderSheet, couponUseRequest, couponMap, memberInfo)) {
                return new OrderSheetCheckResponse("???????????? ?????? ?????? ????????? ???????????? ????????????.", 4,
                    Boolean.FALSE); // ???????????? ?????? ?????? ????????? ???????????? ??????
            }


            List<Long> productNoList = orderSheet.getCartDtoList().stream().map(cartDto -> {
                cartDtoMap.put(cartDto.getProductNo(), cartDto);
                return cartDto.getProductNo();
            }).collect(Collectors.toList());
            Map<Long, Long> productMap = new HashMap<>();
            List<Product> productList =
                productService.retrieveProductListByProductNoList(productNoList);
            if (productList.size() != productNoList.size()) {
                return new OrderSheetCheckResponse("????????? ??? ?????? ????????? ???????????? ????????????.", 4, Boolean.FALSE);
            }
            AtomicReference<Long> productDiscountedTotalPrice =
                new AtomicReference<>(0L); // ??????????????? ????????? ???????????? ??????
            AtomicReference<Long> productTotalPrice = new AtomicReference<>(0L); // ?????? ????????????

            //?????????????????? ???????????? ???????????? ?????? ?????? ??? ??? ????????? ??????
            parseProductListData(cartDtoMap, pointAccumulateSum, couponMap, productMap, productList,
                productDiscountedTotalPrice, productTotalPrice);
            long paymentPrice;
            long discountedTotalPrice;

            MultiValueMap<String, Long> productCategoryMap = new LinkedMultiValueMap<>();
            MultiValueMap<Long, Long> categoryListMap =
                categoryProductService.retrieveCategoryIdListMultiValueMapByProductIdList(
                    productNoList);
            //???????????????????????? ?????????????????? ????????????????????? ??????????????? ???????????? ???????????? ?????? ??????
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
                couponMap.get(null); // ????????? ???????????? ??????
            long orderCouponDiscountSum = getOrderCouponDiscountSum(productMap, productCategoryMap,
                orderCouponList); // ???????????? ?????????
            //????????? ????????????
            discountedTotalPrice =
                Math.max(productDiscountedTotalPrice.get() - orderCouponDiscountSum, 0);
            //????????? ???????????? - ?????? ????????? + ????????? + ?????????
            paymentPrice = discountedTotalPrice - orderSheet.getUsingPoint() +
                (productTotalPrice.get() > 30000 ? 0 : 3000)
                + orderSheet.getGiftWrappingPrice();

            if (paymentPrice == orderSheet.getPaymentAmount()) {//?????? ????????? ??????????????? ?????? ????????? ??????????????? ??????
                updateOrderSheet(orderSheet, cartDtoMap, productNoList, couponUseRequest,
                    productList);
                orderSheet.setMemberNo(memberInfo.getMemberNo());
                orderSheet.setPointAccumulate(pointAccumulateSum.get().intValue());
                return orderSheet;
            }
            return new OrderSheetCheckResponse("?????? ????????? ????????? ????????? ?????????????????????.", 4, Boolean.FALSE);
        }catch (Exception e){
            return new OrderSheetCheckResponse("????????? ????????? ??? ??? ?????? ????????? ?????????????????????.", 5, Boolean.FALSE);
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
        if (coupon.getTypeName().equals("????????????")) {
            long amount = coupon.getAmount() * price / 100;
            return amount > coupon.getMaximumDiscountAmount()?coupon.getMaximumDiscountAmount():amount;
        } else if (coupon.getTypeName().equals("????????????")) {
            return Integer.toUnsignedLong(coupon.getAmount());
        }
        return 0L;
    }

    private void updateOrderSheet(OrderSheet orderSheet, Map<Long, CartDto> cartDtoMap,
                                  List<Long> productNoList, CouponUseRequest couponUseRequest,
                                  List<Product> productList) {
        //product Map ??????/???????????? ?????? ??????
        Map<Long, Product> productListMap = new HashMap<>();
        List<OrderProductDto> orderProductDtoList = new ArrayList<>();
        productList.forEach(product -> {
            productListMap.put(product.getId(), product);
            orderProductDtoList.add(new OrderProductDto(product.getId(), product.getTitle(),
                cartDtoMap.get(product.getId()).getCount(), product.getPrice().intValue()));
        });
        //subscribe ?????? ??????
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
        //??????
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
     * ?????? ????????? ?????? ???????????? ????????????
     * @return true ????????? ?????? ????????? / false ???????????? ?????? ??????????????? ??????
     */
    private boolean couponDataGetAndParsing(OrderSheet orderSheet, CouponUseRequest couponUseRequest, MultiValueMap<Long, CouponRetrieveResponseFromProduct> couponMap, MemberInfo memberInfo) {
        if (orderSheet.getCouponCodeList()!=null){
            MultiValueMap<String, String> multiValueMap = ControllerUtil.getMemberInfoMultiValueMap(memberInfo);
            multiValueMap.put("couponCodeList", orderSheet.getCouponCodeList());
            ApiEntity<List<CouponRetrieveResponseFromProduct>> apiEntity = restService.get(
                gatewayIp + "/coupon/v1/coupons/codes", multiValueMap,
                new ParameterizedTypeReference<>() {});
            //?????? ?????? ??????
            if (apiEntity.getBody() == null || orderSheet.getCouponCodeList().size() != apiEntity.getBody().size()){
                return false;
            }
            apiEntity.getBody().forEach(couponRetrieveResponseFromProduct -> {
                couponMap.add(couponRetrieveResponseFromProduct.getProductNo(), couponRetrieveResponseFromProduct);
                //??????????????????
                if (couponRetrieveResponseFromProduct.getProductNo()!=null){
                    couponUseRequest.getProductCouponList().add(new CouponUsingDto(
                        couponRetrieveResponseFromProduct.getCouponCode(),
                        couponRetrieveResponseFromProduct.getId(),
                        couponRetrieveResponseFromProduct.getProductNo()));
                //??????????????????
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
        Map<Long, Long> productNoOrderProductMap = new HashMap<>();
        //?????? ?????? ??????
        if (!orderSheet.getOrderProductDtoList().isEmpty()) {
            orderSheet.getOrderProductDtoList().forEach(orderProductDto ->
                {
                    OrderProduct orderProduct = orderProductService.saveOrderProduct(orderProductDto, order.getId());
                    productNoOrderProductMap.put(orderProduct.getProductNo(), orderProduct.getId());
                }
            );
        }
        //?????? ????????? ????????????
        if (!orderSheet.getCartDtoList().isEmpty()) {
            deliveryDetailService.saveDeliveryDetailFromOrderSheet(orderSheet, order.getId());
        }
        //?????? ?????? ??????
        if (orderSheet.getSubscribeProductList() != null){
            orderSheet.getSubscribeProductList().forEach(subscribeDto ->
                orderSubscribeService.saveOrderSubscribe(subscribeDto, order.getId())
            );
        }
        //????????? ??????
        if (orderSheet.getUsingPoint()>0){
            pointHistoryService.createPointHistory(new PointHistoryCreateRequest(
                    order.getMemberNo(), -orderSheet.getUsingPoint().intValue(), "????????? ??????(?????? ??????)"));
        }
        //?????? ????????????
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
