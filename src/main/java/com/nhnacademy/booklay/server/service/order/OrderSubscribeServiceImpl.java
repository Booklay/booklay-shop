package com.nhnacademy.booklay.server.service.order;


import com.nhnacademy.booklay.server.dto.order.SubscribeDto;
import com.nhnacademy.booklay.server.entity.OrderSubscribe;
import com.nhnacademy.booklay.server.repository.order.OrderSubscribeRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSubscribeServiceImpl implements OrderSubscribeService{

    private final OrderSubscribeRepository subscribeRepository;

    @Override
    public OrderSubscribe retrieveOrderSubscribe(Long orderSubscribeNo){
        return subscribeRepository.findById(orderSubscribeNo).orElse(OrderSubscribe.builder()
            .build());
    }

    @Override
    public OrderSubscribe saveOrderSubscribe(SubscribeDto subscribeDto, Long orderNo){
        return subscribeRepository.save(OrderSubscribe.builder()
            .subscribeNo(subscribeDto.getSubscribeNo())
            .orderNo(orderNo)
            .amounts(subscribeDto.getCount())
            .price(subscribeDto.getPrice())
            .startAt(LocalDate.now())
            .finishAt(LocalDate.now().plusDays(subscribeDto.getCount()))
            .build());
    }

    @Override
    public OrderSubscribe renewOrderSubscribe(Long subScribeNo, Integer renewDay){
        OrderSubscribe orderSubscribe = retrieveOrderSubscribe(subScribeNo);
        return subscribeRepository.save(OrderSubscribe.builder()
            .id(orderSubscribe.getId())
            .subscribeNo(orderSubscribe.getSubscribeNo())
            .orderNo(orderSubscribe.getOrderNo())
            .amounts(orderSubscribe.getAmounts()+renewDay)
            .price(orderSubscribe.getPrice())
            .startAt(LocalDate.now())
            .finishAt(LocalDate.now().plusDays(orderSubscribe.getAmounts()+renewDay))
            .build());
    }
}
