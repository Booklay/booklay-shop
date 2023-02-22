package com.nhnacademy.booklay.server.repository.order;

import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<OrderListRetrieveResponse> findAllByMemberNoAndIsBlinded(Long memberNo, Boolean isBlind, Pageable pageable);

    Optional<Order> findByIdAndMemberNo(Long orderNo, Long memberNo);
}
