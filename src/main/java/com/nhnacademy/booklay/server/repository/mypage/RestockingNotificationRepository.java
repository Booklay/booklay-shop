package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.entity.RestockingNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestockingNotificationRepository
    extends JpaRepository<RestockingNotification, RestockingNotification.Pk>, RestockingNotificationRepositoryCustom {
}
