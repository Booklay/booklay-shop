package com.nhnacademy.booklay.server.controller.order;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.order.payment.OrderReceipt;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheetSaveResponse;
import com.nhnacademy.booklay.server.dto.order.response.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.response.OrderReceiptSaveResponse;
import com.nhnacademy.booklay.server.exception.order.CheckOrderException;
import com.nhnacademy.booklay.server.exception.order.SaveOrderException;
import com.nhnacademy.booklay.server.exception.service.NotEnoughStockException;
import com.nhnacademy.booklay.server.service.category.CategoryProductService;
import com.nhnacademy.booklay.server.service.order.ComplexOrderService;
import com.nhnacademy.booklay.server.service.order.OrderService;
import com.nhnacademy.booklay.server.service.order.RedisOrderService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    private final OrderService orderService;

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

    /**
     * 주문내역이 유효한지 체크
     * @param orderSheet 주문내역
     */
    @PostMapping("check")
    public ResponseEntity<OrderSheetSaveResponse> saveOrderSheet(@RequestBody OrderSheet orderSheet, MemberInfo memberInfo){
        OrderSheet updatedOrderSheet = complexOrderService.checkOrder(orderSheet, memberInfo);
        String uuid = redisOrderService.saveOrderSheet(updatedOrderSheet);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON).body(
                new OrderSheetSaveResponse(uuid, Boolean.TRUE, updatedOrderSheet.getPaymentAmount(),
                    null));
    }

    @ExceptionHandler({CheckOrderException.class})
    public ResponseEntity<OrderSheetSaveResponse> checkOrderExceptionResponse(Exception e){
        OrderSheetSaveResponse orderReceiptSaveResponse =
            new OrderSheetSaveResponse(null , false, null, e.getMessage());
        return ResponseEntity.ok(orderReceiptSaveResponse);
    }
    /**
     * 주문작업 처리
     * 주문기록에서 정보를 가져와 영수증으로 저장
     */
    @GetMapping("payment/{orderId}")
    public ResponseEntity<OrderReceiptSaveResponse> payOrder(@PathVariable String orderId,
                                                         MemberInfo memberInfo){
        OrderReceiptSaveResponse orderReceiptSaveResponse =  complexOrderService.payOrder(orderId, memberInfo);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderReceiptSaveResponse);
    }

    @ExceptionHandler({SaveOrderException.class ,NotEnoughStockException.class})
    public ResponseEntity<OrderReceiptSaveResponse> orderExceptionResponse(Exception e){
        OrderReceiptSaveResponse orderReceiptSaveResponse =
            new OrderReceiptSaveResponse(true, e.getMessage());
        return ResponseEntity.ok(orderReceiptSaveResponse);
    }



    @GetMapping("receipt/{orderNo}")
    public ResponseEntity<OrderReceipt> retrieveOrderReceipt(@PathVariable Long orderNo, MemberInfo memberInfo){

        return ResponseEntity.ok(complexOrderService.retrieveOrderReceipt(orderNo, memberInfo.getMemberNo()));
    }

    @GetMapping("receipt/list")
    public ResponseEntity<PageResponse<OrderListRetrieveResponse>> retrieveOrderReceiptPage(MemberInfo memberInfo, Pageable pageable){
        return ResponseEntity.ok(orderService.retrieveOrderListRetrieveResponsePageByMemberNoAndBlind(
            memberInfo.getMemberNo(), Boolean.FALSE, pageable));
    }

    @GetMapping("confirm/{orderNo}")
    public ResponseEntity<Boolean> confirmOrder(@PathVariable Long orderNo, MemberInfo memberInfo){
        return ResponseEntity.ok(orderService.confirmOrder(orderNo, memberInfo.getMemberNo()));
    }

    @GetMapping("stat/{month}")
    public ResponseEntity<List<List<Long>>> orderStat(@PathVariable Integer month){
        return ResponseEntity.ok(orderService.retrieveOrderStat(month));
    }



}
