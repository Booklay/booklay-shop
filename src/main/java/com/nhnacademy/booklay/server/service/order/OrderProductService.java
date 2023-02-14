package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.order.OrderProductDto;
import com.nhnacademy.booklay.server.entity.OrderProduct;
import java.util.List;

public interface OrderProductService {
    OrderProduct retrieveOrderProduct(Long orderProductNo);

    List<OrderProduct> retrieveOrderProductListByOrderNo(Long orderNo);

    List<OrderProduct> retrieveOrderProductListByProductNo(Long productNo);

    Integer countAllOrderProductCountByProductNo(Long productNo);

    Integer countOrderProductByProductNo(Long productNo);

    OrderProduct saveOrderProduct(OrderProductDto orderProductDto, Long orderNo);

    void deleteOrderProduct(Long orderNo);
}
