package com.nhnacademy.booklay.server.service.product.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.request.DisAndConnectBookWithSubscribeRequest;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.BookSubscribe;
import com.nhnacademy.booklay.server.entity.BookSubscribe.Pk;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.product.BookSubscribeRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class BookSubscribeServiceImplTest {

  @InjectMocks
  BookSubscribeServiceImpl bookSubscribeService;
  @Mock
  BookSubscribeRepository bookSubscribeRepository;
  @Mock
  SubscribeRepository subscribeRepository;
  @Mock
  ProductRepository productRepository;
  @Mock
  ProductService productService;

  DisAndConnectBookWithSubscribeRequest request;
  CreateUpdateProductBookRequest bookRequest;
  Product product;
  CreateUpdateProductSubscribeRequest subscribeRequest;
  Subscribe subscribe;
  BookSubscribe bookSubscribe;

  @BeforeEach
  void setUp(){
    request = new DisAndConnectBookWithSubscribeRequest(1L, 1L);
    request.setReleaseDate(LocalDate.now());

    bookRequest = DummyCart.getDummyProductBookDto();
    product = DummyCart.getDummyProduct(bookRequest);
    subscribeRequest = DummyCart.getDummyProductSubscribeDto();
    subscribe = DummyCart.getDummySubscribe(subscribeRequest);

    bookSubscribe = BookSubscribe.builder()
        .subscribeNo(subscribe)
        .releaseDate(request.getReleaseDate())
        .productNo(product)
        .build();
  }

  @Test
  void bookSubscribeConnection() {
    given(subscribeRepository.findById(request.getSubscribeId())).willReturn(Optional.ofNullable(subscribe));
    given(productRepository.findById(request.getProductId())).willReturn(Optional.ofNullable(product));

    BookSubscribe.Pk pk = new Pk(request.getSubscribeId(), request.getProductId());

    bookSubscribeService.bookSubscribeConnection(request);

    BDDMockito.then(bookSubscribeRepository).should().save(any());
  }

  @Test
  void bookSubscribeDisconnection() {
    //given
    BookSubscribe.Pk pk = new Pk(request.getSubscribeId(), request.getProductId());
    given(bookSubscribeRepository.existsById(pk)).willReturn(true);

    //when
    bookSubscribeService.bookSubscribeDisconnection(request);

    //then
    BDDMockito.then(bookSubscribeRepository).should().deleteById(pk);
  }

  @Test
  void retrieveBookSubscribe() throws IOException {
    given(bookSubscribeRepository.findBooksProductIdBySubscribeId(request.getSubscribeId())).willReturn(any());

    bookSubscribeService.retrieveBookSubscribe(request.getSubscribeId());

    BDDMockito.then(productService).should().retrieveProductResponses(any());
  }
}