package com.nhnacademy.booklay.server.repository.order;

import com.nhnacademy.booklay.server.entity.OrderSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSubscribeRepository extends JpaRepository<OrderSubscribe, Long> {
    void deleteAllByOrderNo(Long orderNo);
}
