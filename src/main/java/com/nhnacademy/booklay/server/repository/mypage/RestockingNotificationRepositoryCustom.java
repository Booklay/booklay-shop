package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.entity.RestockingNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RestockingNotificationRepositoryCustom {
  Page<RestockingNotification> retrieveRegister(Long memberId, Pageable pageable);
}
