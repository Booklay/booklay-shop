package com.nhnacademy.booklay.server.repository.order;

import com.nhnacademy.booklay.server.dto.order.OrderListRetrieveResponse;
import com.nhnacademy.booklay.server.entity.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<OrderListRetrieveResponse> findAllByMemberNoAndIsBlindedOrderByOrderedAtDesc(Long memberNo, Boolean isBlind, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from `order` where month(ordered_at) = :month")
    List<Order> findAllByOrderedAtMonth(Integer month);

    Optional<Order> findByIdAndMemberNo(Long orderNo, Long memberNo);
}
