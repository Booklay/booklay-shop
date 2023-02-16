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
            .finishAt(LocalDate.now().plusMonths(subscribeDto.getCount()))
            .build());
    }

    /**
     * 구독기간 연장시 사용
     * @param orderSubScribeNo 키
     * @param renewMonth 추가된 개월 수
     * @return
     */
    @Override
    public OrderSubscribe renewOrderSubscribe(Long orderSubScribeNo, Integer renewMonth){
        OrderSubscribe orderSubscribe = retrieveOrderSubscribe(orderSubScribeNo);
        return subscribeRepository.save(OrderSubscribe.builder()
            .id(orderSubscribe.getId())
            .subscribeNo(orderSubscribe.getSubscribeNo())
            .orderNo(orderSubscribe.getOrderNo())
            .amounts(orderSubscribe.getAmounts()+renewMonth)
            .price(orderSubscribe.getPrice())
            .startAt(LocalDate.now())
            .finishAt(LocalDate.now().plusMonths((long)orderSubscribe.getAmounts()+renewMonth))
            .build());
    }

    /**
     * 환불시 삭제
     */
    @Override
    public void deleteOrderSubscribeByOrderNo(Long orderNo){
        subscribeRepository.deleteAllByOrderNo(orderNo);
    }
}
