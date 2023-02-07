package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestockingNotificationService {

  void createAlarm(CreateDeleteWishlistAndAlarmRequest request);

  void deleteAlarm(CreateDeleteWishlistAndAlarmRequest request);

  Page<RetrieveProductResponse> retrievePage(Long memberId, Pageable pageable) throws IOException;
}
