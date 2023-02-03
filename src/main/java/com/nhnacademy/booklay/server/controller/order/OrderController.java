package com.nhnacademy.booklay.server.controller.order;

import com.nhnacademy.booklay.server.dto.cart.CartRetrieveResponse;
import com.nhnacademy.booklay.server.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("products")
    public ResponseEntity<List<CartRetrieveResponse>> getProductDataByProductList(
            @RequestParam("productNoList") List<Long> productNoList) {
        List<CartRetrieveResponse> cartList = productService.retrieveProductListByProductNoList(productNoList).stream()
                .map(product -> new CartRetrieveResponse(product.getId(), product.getTitle(), product.getPrice(), 0)).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(cartList);
    }
}
