package com.nhnacademy.booklay.server.service.delivery;

import com.nhnacademy.booklay.server.entity.DeliveryStatusCode;
import com.nhnacademy.booklay.server.repository.delivery.DeliveryStatusCodeRepository;
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
public class DeliveryStatusCodeServiceImpl implements DeliveryStatusCodeService{
    private Map<Integer, String> cachedCode;
    private final DeliveryStatusCodeRepository deliveryStatusCodeRepository;

    @Override
    public String retrieveOrderStatusCodeName(Integer deliveryStatusCodeNo){
        if (!cachedCode.containsKey(deliveryStatusCodeNo)){
            updateCachedCode();
        }
        return cachedCode.get(deliveryStatusCodeNo);
    }

    @Override
    public DeliveryStatusCode saveOrderStatusCode(DeliveryStatusCode deliveryStatusCode){
        DeliveryStatusCode deliveryStatusCode1 = deliveryStatusCodeRepository.save(deliveryStatusCode);
        updateCachedCode();
        return deliveryStatusCode1;
    }

    @Override
    public void deleteOrderStatusCode(Integer deliveryStatusCodeNo){
        deliveryStatusCodeRepository.deleteById(deliveryStatusCodeNo);
        updateCachedCode();
    }

    @PostConstruct
    @Scheduled(cron = "0/10 * * * * *")
    private void updateCachedCode(){
        List<DeliveryStatusCode> deliveryStatusCodeList = deliveryStatusCodeRepository.findAll();
        Map<Integer, String> newCachedCode = new HashMap<>();
        newCachedCode.put(null, null);
        deliveryStatusCodeList.forEach(deliveryStatusCode -> newCachedCode.put(deliveryStatusCode.getId(), deliveryStatusCode.getName()));
        cachedCode = newCachedCode;
    }
}
