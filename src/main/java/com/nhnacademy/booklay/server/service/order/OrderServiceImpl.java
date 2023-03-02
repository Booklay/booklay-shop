package com.nhnacademy.booklay.server.service.order;


import com.nhnacademy.booklay.server.dto.PageResponse;
import com.nhnacademy.booklay.server.dto.member.request.PointHistoryCreateRequest;
import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.dto.order.payment.OrderSheet;
import com.nhnacademy.booklay.server.entity.Order;
import com.nhnacademy.booklay.server.repository.order.OrderRepository;
import com.nhnacademy.booklay.server.service.mypage.PointHistoryService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
            new PageResponse<>(orderRepository.findAllByMemberNoAndIsBlindedOrderByOrderedAtDesc(memberNo, isBlind, pageable));
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

    @Override
    public List<List<Long>> retrieveOrderStat(Integer month) {
        List<Order> orderList = orderRepository.findAllByOrderedAtMonth(month);
        MultiValueMap<Integer, Order> multiValueMap = new LinkedMultiValueMap<>();
        for (Order order : orderList){
            multiValueMap.add(order.getOrderedAt().getDayOfMonth(), order);
        }
        List<List<Long>> returnList = new ArrayList<>();
        for (int i = 0; i <= LocalDateTime.now().withDayOfMonth(1).plusMonths(1).minusDays(1).getDayOfMonth()+1;i++){
            List<Long> list = new ArrayList<>();
            list.add((long) i);
            list.add(0L);//payment
            list.add(0L);//discounted
            list.add(0L);//productPrice
            if (multiValueMap.get(i) != null){
                for (Order order: Objects.requireNonNull(multiValueMap.get(i))){
                    list.set(1, list.get(1) + order.getPaymentPrice());
                    list.set(2, list.get(2) + order.getDiscountPrice());
                    list.set(3, list.get(3) + order.getProductPriceSum());
                }
            }
            returnList.add(list);
        }
        return returnList;
    }

}
