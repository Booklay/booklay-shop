package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {
    private final SubscribeRepository subscribeRepository;

    @Override
    public List<Subscribe> retrieveSubscribeListByProductNoList(List<Long> productNoList){
        return subscribeRepository.findAllByProductNoIn(productNoList);
    }
    @Override
    public Subscribe retrieveSubscribeByProductNo(Long productNo){
        return subscribeRepository.findById(productNo).orElse(null);
    }
}
