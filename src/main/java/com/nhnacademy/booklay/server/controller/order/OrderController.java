package com.nhnacademy.booklay.server.controller.order;

import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.common.MemberInfo;
import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderReceipt;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheetCheckResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheetSaveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.StorageRequest;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.service.category.CategoryProductService;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import com.nhnacademy.booklay.server.service.order.ComplexOrderService;
import com.nhnacademy.booklay.server.service.order.OrderService;
import com.nhnacademy.booklay.server.service.order.RedisOrderService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final ProductService productService;
    private final RedisOrderService redisOrderService;
    private final CategoryProductService categoryProductService;
    private final ComplexOrderService complexOrderService;
    private final PointHistoryService pointHistoryService;
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

    @PostMapping("check")
    public ResponseEntity<OrderSheetSaveResponse> saveOrderSheet(@RequestBody OrderSheet orderSheet, MemberInfo memberInfo){
        OrderSheet updatedOrderSheet = complexOrderService.checkOrder(orderSheet, memberInfo);
        if (updatedOrderSheet instanceof OrderSheetCheckResponse){
            return ResponseEntity.status(HttpStatus.OK)
                .body(new OrderSheetSaveResponse(null , false, null
                    , ((OrderSheetCheckResponse) updatedOrderSheet).getReason(),
                    ((OrderSheetCheckResponse) updatedOrderSheet).getReasonType()));
        }
        String uuid = redisOrderService.saveOrderSheet(updatedOrderSheet);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new OrderSheetSaveResponse(uuid, Boolean.TRUE, updatedOrderSheet.getPaymentAmount(), null, null));
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
        if (orderSheet.getOrderNo() == null){
            Order order = complexOrderService.saveReceipt(orderSheet);
            return ResponseEntity.status(HttpStatus.CREATED).body(order.getId());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(orderSheet.getOrderNo());

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

}
