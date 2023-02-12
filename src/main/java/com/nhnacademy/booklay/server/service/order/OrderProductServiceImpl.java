package com.nhnacademy.booklay.server.service.order;

import com.nhnacademy.booklay.server.dto.order.OrderProductDto;
import com.nhnacademy.booklay.server.entity.OrderProduct;
import com.nhnacademy.booklay.server.repository.order.OrderProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderProductServiceImpl implements OrderProductService{
    private final OrderProductRepository orderProductRepository;


    @Override
    public OrderProduct retrieveOrderProduct(Long orderProductNo){
        return orderProductRepository.findById(orderProductNo).orElse(OrderProduct.builder().build());
    }

    @Override
    public List<OrderProduct> retrieveOrderProductListByOrderNo(Long orderNo){
        return orderProductRepository.findAllByOrderNo(orderNo);
    }

    @Override
    public List<OrderProduct> retrieveOrderProductListByProductNo(Long productNo){
        return orderProductRepository.findAllByProductNo(productNo);
    }

    /**
     * 상품번호로 모든 판매수량 세기
     * @return 판매수량
     */
    @Override
    public Integer countAllOrderProductCountByProductNo(Long productNo){
        return orderProductRepository.findAllByProductNo(productNo).stream()
            .map(OrderProduct::getCount)
            .reduce(Integer::sum)
            .orElse(null);
    }
    /**
     * 상품번호로 판매횟수 세기
     * @return 판매된 횟수 O / 판매된 수량 X
     */
    @Override
    public Integer countOrderProductByProductNo(Long productNo){
        return orderProductRepository.countAllByProductNo(productNo);
    }

    @Override
    public OrderProduct saveOrderProduct(OrderProductDto orderProductDto, Long orderNo){
        return orderProductRepository.save(OrderProduct.builder()
            .orderNo(orderNo)
            .productNo(orderProductDto.getProductNo())
            .count(orderProductDto.getCount())
            .price(Math.toIntExact(orderProductDto.getPrice()))
            .build()
        );
    }

    @Override
    public void deleteOrderProduct(Long orderNo){
        orderProductRepository.deleteAllByOrderNo(orderNo);
    }

}
