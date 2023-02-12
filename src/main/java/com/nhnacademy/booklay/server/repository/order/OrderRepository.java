package com.nhnacademy.booklay.server.repository.order;

import com.nhnacademy.booklay.server.dto.order.OrderRecipe;
import com.nhnacademy.booklay.server.entity.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<OrderRecipe> findByMemberNoAndId(Long memberNo, Long orderNo);
}
