package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.dto.order.SubscribeDto;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.service.delivery.DeliveryDetailService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComplexOrderServiceImpl implements ComplexOrderService{
    private final OrderService orderService;
    private final DeliveryDetailService deliveryDetailService;
    private final ProductService productService;
    private final SubscribeService subscribeService;
    @Override
    public OrderSheet checkOrder(OrderSheet orderSheet) {
        Map<Long, Integer> cartDtoMap = new HashMap<>();
        List<Long> productNoList = orderSheet.getCartDtoList().stream().map(cartDto -> {
            cartDtoMap.put(cartDto.getProductNo(), cartDto.getCount());
            return cartDto.getProductNo();
        }).collect(Collectors.toList());

        Map<Long, Product> productListMap = new HashMap<>();
        List<Product> productList = productService.retrieveProductListByProductNoList(productNoList);
        List<Subscribe> subscribeList = subscribeService.retrieveSubscribeListByProductNoList(productNoList);
        List<SubscribeDto> subscribeCartDtoList = new ArrayList<>();
        productList.forEach(product -> productListMap.put(product.getId(), product));
        subscribeList.forEach(subscribe -> {
            if (productListMap.containsKey(subscribe.getProductNo())){
                subscribeCartDtoList.add(new SubscribeDto(subscribe.getId(), cartDtoMap.get(subscribe.getProductNo()), productListMap.get(subscribe.getProductNo()).getPrice()));
                orderSheet.getCartDtoList().remove(cartDtoMap.get(subscribe.getProductNo()));
            }
        });
        orderSheet.setSubscribeProductList(subscribeCartDtoList);
        //todo 주문 유효 체크
        if (true){
            return orderSheet;
        }
        return null;
    }

    @Override
    public Order saveReceipt(OrderSheet orderSheet){
        Order order = orderService.saveOrder(orderSheet);
        if (!orderSheet.getCartDtoList().isEmpty()){
            deliveryDetailService.saveDeliveryDetailFromOrderSheet(orderSheet, order.getId());
        }
        if (!orderSheet.getSubscribeProductList().isEmpty()){
            orderSheet.getSubscribeProductList().forEach(subscribeDto -> {

            });
        }


        return order;
    }
}
