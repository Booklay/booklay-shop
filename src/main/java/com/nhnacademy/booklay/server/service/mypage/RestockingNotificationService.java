package com.nhnacademy.booklay.server.service.mypage;

import com.nhnacademy.booklay.server.dto.product.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RestockingNotificationService {

  void createAlarm(WishlistAndAlarmRequest request);

  void deleteAlarm(WishlistAndAlarmRequest request);

  Page<RetrieveProductResponse> retrievePage(Long memberId, Pageable pageable) throws IOException;
}
