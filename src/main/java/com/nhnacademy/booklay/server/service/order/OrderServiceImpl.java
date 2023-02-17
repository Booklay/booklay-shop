package com.nhnacademy.booklay.server.service.order;


import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;
    @Override
    public Order retrieveOrder(Long orderNo){
        return orderRepository.findById(orderNo).orElse(Order.builder().build());
    }

    @Override
    public PageResponse<OrderListRetrieveResponse> retrieveOrderListRetrieveResponsePageByMemberNoAndBlind(Long memberNo, Boolean isBlind, Pageable pageable){
        PageResponse<OrderListRetrieveResponse> orderListRetrieveResponsePageResponse =
            new PageResponse<>(orderRepository.findAllByMemberNoAndIsBlinded(memberNo, isBlind, pageable));
        orderListRetrieveResponsePageResponse.getData().forEach(response ->
            response.setOrderStatusName(orderStatusService.retrieveOrderStatusCodeName(response.getOrderStatusCodeNo())));
        return orderListRetrieveResponsePageResponse;
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
            .isBlinded(Boolean.FALSE)
            .orderTitle(orderSheet.getOrderTitle())
            .pointAccumulate(orderSheet.getPointAccumulate())
            .build();
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long orderNo){
        orderRepository.deleteById(orderNo);
    }


}
