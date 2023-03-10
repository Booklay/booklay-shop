package com.nhnacademy.booklay.server.repository.delivery;

import com.nhnacademy.booklay.server.dto.order.payment.OrderReceipt;
import com.nhnacademy.booklay.server.entity.DeliveryDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Long> {
    Optional<OrderReceipt> findByOrder_MemberNoAndOrder_Id(Long memberNo, Long orderNo);

    List<DeliveryDetail> findAllByOrder_MemberNoAndOrder_Id(Long memberNo, Long orderNo);
}
