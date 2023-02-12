package com.nhnacademy.booklay.server.repository.order;

import com.nhnacademy.booklay.server.entity.OrderProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findAllByOrderNo(Long orderNo);
    List<OrderProduct> findAllByProductNo(Long productNo);
    Integer countAllByProductNo(Long productNo);

    void deleteAllByOrderNo(Long orderNo);
}
