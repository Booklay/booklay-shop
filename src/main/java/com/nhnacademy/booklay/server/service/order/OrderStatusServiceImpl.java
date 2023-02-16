package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.entity.OrderStatusCode;
import com.nhnacademy.booklay.server.repository.order.OrderStatusCodeRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class OrderStatusServiceImpl implements OrderStatusService{

    private Map<Long, String> cachedCode;
    private final OrderStatusCodeRepository orderStatusCodeRepository;


    @Override
    public String retrieveOrderStatusCodeName(Long orderStatusCodeNo){
        if (!cachedCode.containsKey(orderStatusCodeNo)){
            updateCachedCode();
        }
        return cachedCode.get(orderStatusCodeNo);
    }

    @Override
    public OrderStatusCode saveOrderStatusCode(OrderStatusCode orderStatusCode){
        OrderStatusCode orderStatusCode1 = orderStatusCodeRepository.save(orderStatusCode);
        updateCachedCode();
        return orderStatusCode1;
    }

    @Override
    public void deleteOrderStatusCode(Long orderStatusCodeNo){
        orderStatusCodeRepository.deleteById(orderStatusCodeNo);
        updateCachedCode();
    }

    @PostConstruct
    @Scheduled(cron = "0/10 * * * * *")
    private void updateCachedCode(){
        List<OrderStatusCode> orderStatusCodeList = orderStatusCodeRepository.findAll();
        Map<Long, String> newCachedCode = new HashMap<>();
        newCachedCode.put(null, null);
        orderStatusCodeList.forEach(orderStatusCode -> newCachedCode.put(orderStatusCode.getId(), orderStatusCode.getName()));
        cachedCode = newCachedCode;
    }
}
