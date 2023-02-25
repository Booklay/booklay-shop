package com.nhnacademy.booklay.server.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.nhnacademy.booklay.server.dto.order.OrderProductDto;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.OrderProduct;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.repository.order.OrderProductRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @Author: 최규태
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class OrderProductServiceImplTest {

  @InjectMocks
  OrderProductServiceImpl orderProductService;

  @Mock
  OrderProductRepository orderProductRepository;

  Long productNo;
  Long orderNo;
  Long orderProductNo;
  OrderProduct orderProduct;
  List<OrderProduct> orderProductList;

  @BeforeEach
  void setUp() {
    productNo = 1L;
    orderNo = 1L;
    orderProductNo = 1L;
    orderProduct = Dummy.getDummyOrderProduct();

    orderProductList = new ArrayList<>();
    orderProductList.add(orderProduct);
  }

  @Test
  void retrieveOrderProduct() {
    //given
    given(orderProductRepository.findById(orderProductNo)).willReturn(
        Optional.ofNullable(orderProduct));

    //when
    OrderProduct actual = orderProductService.retrieveOrderProduct(orderProductNo);

    //then
    assertThat(actual.getProductNo()).isEqualTo(orderProduct.getProductNo());
  }

  @Test
  void retrieveOrderProductListByOrderNo() {
    //given
    given(orderProductRepository.findAllByOrderNo(orderNo)).willReturn(orderProductList);

    //when
    List<OrderProduct> actual = orderProductService.retrieveOrderProductListByOrderNo(orderNo);

    //then
    assertThat(actual).hasSameSizeAs(orderProductList);
    assertThat(actual.get(0).getOrderNo()).isEqualTo(orderProductList.get(0).getOrderNo());
  }

  @Test
  void retrieveOrderProductListByProductNo() {
    //given
    given(orderProductRepository.findAllByProductNo(productNo)).willReturn(orderProductList);

    //when
    List<OrderProduct> actual = orderProductService.retrieveOrderProductListByProductNo(productNo);

    //then
    assertThat(actual).hasSameSizeAs(orderProductList);
    assertThat(actual.get(0).getProductNo()).isEqualTo(orderProductList.get(0).getProductNo());

  }

  @Test
  void countAllOrderProductCountByProductNo() {
    //given
    Integer expect = orderProductList.stream()
        .map(OrderProduct::getCount)
        .reduce(Integer::sum)
        .orElse(null);

    given(orderProductRepository.findAllByProductNo(productNo)).willReturn(orderProductList);
    //when
    Integer actual = orderProductService.countAllOrderProductCountByProductNo(productNo);

    assertThat(actual).isEqualTo(expect);
  }

  @Test
  void countOrderProductByProductNo() {

    orderProductService.countOrderProductByProductNo(productNo);

    then(orderProductRepository).should().countAllByProductNo(productNo);

  }

  @Test
  void saveOrderProduct() {
    Product product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    ReflectionTestUtils.setField(orderProduct, "product", product);
    OrderProductDto orderProductDto = new OrderProductDto(orderProduct);

    //when
    OrderProduct actual = orderProductService.saveOrderProduct(orderProductDto, orderNo);

    OrderProduct expect = OrderProduct.builder()
        .orderNo(orderNo)
        .productNo(orderProductDto.getProductNo())
        .count(orderProductDto.getCount())
        .price(Math.toIntExact(orderProductDto.getPrice()))
        .build();

    then(orderProductRepository).should().save(any());

  }

  @Test
  void deleteOrderProduct() {

    orderProductService.deleteOrderProduct(orderNo);

    then(orderProductRepository).should().deleteAllByOrderNo(orderNo);

  }
}