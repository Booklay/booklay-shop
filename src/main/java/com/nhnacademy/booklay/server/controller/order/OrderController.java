package com.nhnacademy.booklay.server.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.coupon.request.CouponUseRequest;
import com.nhnacademy.booklay.server.dto.order.OrderReceipt;
import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.OrderSheetSaveResponse;
import com.nhnacademy.booklay.server.dto.order.StorageRequest;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.service.RestService;
import com.nhnacademy.booklay.server.service.category.CategoryProductService;
import com.nhnacademy.booklay.server.service.order.ComplexOrderService;
import com.nhnacademy.booklay.server.service.order.RedisOrderService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final ProductService productService;
    private final RedisOrderService redisOrderService;
    private final CategoryProductService categoryProductService;
    private final ComplexOrderService complexOrderService;

    private final RestService restService;
    private final String gatewayIp;
    private final ObjectMapper objectMapper;
    @GetMapping("products")
    public ResponseEntity<List<CartRetrieveResponse>> getProductDataByProductList(
        @RequestParam("productNoList") List<Long> productNoList) {
        MultiValueMap<Long, Long> multiValueMap =
            categoryProductService.retrieveCategoryIdListMultiValueMapByProductIdList(productNoList);
        List<CartRetrieveResponse> cartList = productService.retrieveProductListByProductNoList(productNoList).stream()
            .map(product -> new CartRetrieveResponse(product.getId(), product.getTitle(), product.getPrice(), 0,
                multiValueMap.get(product.getId()), product.getThumbnailNo())).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
            .contentType(MediaType.APPLICATION_JSON)
            .body(cartList);
    }

    //todo 체크 구현 필요
    @PostMapping("check")
    public ResponseEntity<OrderSheetSaveResponse> saveOrderSheet(@RequestBody OrderSheet orderSheet, MemberInfo memberInfo){
        OrderSheet updatedOrderSheet = complexOrderService.checkOrder(orderSheet, memberInfo);
        if (updatedOrderSheet == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        String uuid = redisOrderService.saveOrderSheet(updatedOrderSheet);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new OrderSheetSaveResponse(uuid, Boolean.TRUE, updatedOrderSheet.getPaymentAmount()));
    }


    @GetMapping("sheet/{orderId}")
    public ResponseEntity<OrderSheet> retrieveOrderSheet(@PathVariable String orderId){
        OrderSheet orderSheet = redisOrderService.retrieveOrderSheet(orderId);
        return ResponseEntity.ok(orderSheet);
    }

    @PostMapping("storage/down")
    public ResponseEntity<Boolean> productStorageDown(@RequestBody StorageRequest storageRequest){
//todo 실제 감소시키기
//        Boolean success = productService.
        return ResponseEntity.ok(Boolean.TRUE);
    }

    /**
     * 주문기록에서 정보를 가져와 영수증으로 저장
     */
    @PostMapping("receipt/{orderId}")
    public ResponseEntity<Long> saveOrderReceipt(@PathVariable String orderId){
        OrderSheet orderSheet = redisOrderService.retrieveOrderSheet(orderId);
        Order order = complexOrderService.saveReceipt(orderSheet);
        //쿠폰 사용전송
        String couponUsingUrl = gatewayIp + "/coupon/v1/coupons/using";
        CouponUseRequest couponUseRequest = orderSheet.getCouponUseRequest();
        couponUseRequest.getCategoryCouponList().forEach(couponUsingDto -> couponUsingDto.setUsedTargetNo(order.getId()));
        restService.post(couponUsingUrl, objectMapper.convertValue(orderSheet.getCouponUseRequest(), Map.class), void.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
    }

    @GetMapping("receipt/{orderNo}")
    public ResponseEntity<OrderReceipt> retrieveOrderReceipt(@PathVariable Long orderNo, MemberInfo memberInfo){

        return ResponseEntity.ok(complexOrderService.retrieveOrderReceipt(orderNo, memberInfo.getMemberNo()));
    }

}
