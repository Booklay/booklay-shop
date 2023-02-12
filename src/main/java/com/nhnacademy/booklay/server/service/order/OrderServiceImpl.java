package com.nhnacademy.booklay.server.service.order;


import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.order.OrderSheet;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.repository.order.OrderRepository;
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
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    @Override
    public Order retrieveOrder(Long orderNo){
        return orderRepository.findById(orderNo).orElse(Order.builder().build());
    }

    @Override
    public Order saveOrder(OrderSheet orderSheet){
        Order order = Order.builder()
            .memberNo(orderSheet.getMemberNo())
            .orderStatusCodeNo(1L)
            .productPriceSum(orderSheet.getProductPriceSum())
            .deliveryPrice(orderSheet.getDeliveryPrice())
            .discountPrice(orderSheet.getDiscountPrice())
            .pointUsePrice(orderSheet.getUsingPoint())
            .paymentPrice(orderSheet.getPaymentAmount())
            .paymentMethod(orderSheet.getPaymentMethod())
            .giftWrappingPrice(orderSheet.getGiftWrappingPrice())
            .build();
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderNo){
        orderRepository.deleteById(orderNo);
    }


}
