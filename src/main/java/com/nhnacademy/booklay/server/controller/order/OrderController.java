package com.nhnacademy.booklay.server.controller.order;

import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.OrderCheckRequest;
import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.OrderSheetSaveResponse;
import com.nhnacademy.booklay.server.service.category.CategoryProductService;
import com.nhnacademy.booklay.server.service.delivery.DeliveryDetailService;
import com.nhnacademy.booklay.server.service.order.ComplexOrderService;
import com.nhnacademy.booklay.server.service.order.OrderService;
import com.nhnacademy.booklay.server.service.order.RedisOrderService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.util.UUID;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final ProductService productService;
    private final RedisOrderService redisOrderService;
    private final CategoryProductService categoryProductService;
    private final DeliveryDetailService deliveryDetailService;
    private final OrderService orderService;
    private final ComplexOrderService complexOrderService;
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
    public ResponseEntity<OrderSheetSaveResponse> saveOrderSheet(@RequestBody
                                                                 OrderSheet orderSheet){
        if (complexOrderService.checkOrder(orderSheet) == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        OrderSheetSaveResponse orderSheetSaveResponse = redisOrderService.saveOrderSheet(orderSheet);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderSheetSaveResponse);
    }


    @GetMapping("sheet/{orderId}")
    public ResponseEntity<OrderSheet> retrieveOrderSheet(@PathVariable String orderId){
        OrderSheet orderSheet = redisOrderService.retrieveOrderSheet(orderId);
        return ResponseEntity.ok(orderSheet);
    }

    /**
     * 주문기록에서 정보를 가져와 영수증으로 저장
     * 비 구독상품만 해당 //todo 구독 상품 고려 필요
     */
    @PostMapping("recipe/{orderId}")
    public ResponseEntity<String> saveOrderRecipe(@PathVariable String orderId){
        OrderSheet orderSheet = redisOrderService.retrieveOrderSheet(orderId);
        complexOrderService.saveReceipt(orderSheet);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
