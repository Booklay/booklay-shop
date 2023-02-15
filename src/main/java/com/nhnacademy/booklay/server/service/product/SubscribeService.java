package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.entity.Subscribe;
import java.util.List;

public interface SubscribeService {

    List<Subscribe> retrieveSubscribeListByProductNoList(List<Long> productNoList);

    Subscribe retrieveSubscribeByProductNo(Long productNo);
}
