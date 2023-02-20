package com.nhnacademy.booklay.server.repository.order;

import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<OrderListRetrieveResponse> findAllByMemberNoAndIsBlinded(Long memberNo, Boolean isBlind, Pageable pageable);

    Optional<Order> findByIdAndOrderStatusCodeNoEqualsAndMemberNo(Long orderNo, Long statusCodeNo, Long memberNo);
}
