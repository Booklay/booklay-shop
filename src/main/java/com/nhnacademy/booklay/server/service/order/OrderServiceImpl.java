package com.nhnacademy.booklay.server.service.order;


import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.repository.order.OrderRepository;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final OrderStatusService orderStatusService;
    private final PointHistoryService pointHistoryService;
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

    @Override
    public Boolean confirmOrder(Long orderNo, Long memberNo) {
        Optional<Order> optionalOrder = orderRepository.findByIdAndMemberNo(orderNo, memberNo);
        if (optionalOrder.isEmpty()) {
            return Boolean.FALSE;
        }
        Order order = optionalOrder.get();
        if (order.getPointAccumulate() != 0) {
            pointHistoryService.createPointHistory(
                new PointHistoryCreateRequest(order.getMemberNo(), order.getPointAccumulate(),
                    "상품 구매 확정"));
        }
        order.setOrderStatusCodeNo(4L);
        orderRepository.save(order);
        return Boolean.TRUE;
    }


}
