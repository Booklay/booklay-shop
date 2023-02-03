package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.RestockingNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestockingNotificationRepository
    extends JpaRepository<RestockingNotification, RestockingNotification.Pk> {
}
