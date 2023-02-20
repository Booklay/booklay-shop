package com.nhnacademy.booklay.server.repository.product.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.BookSubscribe;
import com.nhnacademy.booklay.server.entity.BookSubscribe.Pk;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.repository.ObjectFileRepository;
import com.nhnacademy.booklay.server.repository.product.BookSubscribeRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class BookSubscribeRepositoryImplTest {

  @Autowired
  BookSubscribeRepository bookSubscribeRepository;
  @Autowired
  TestEntityManager entityManager;

  EntityManager em;

  Product bookProduct;
  Product subscribeProduct;
  Subscribe subscribe;
  ObjectFile objectFile;
  BookSubscribe bookSubscribe;
  @Autowired
  private ObjectFileRepository objectFileRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private SubscribeRepository subscribeRepository;

  @BeforeEach
  void setUp() {
    em = entityManager.getEntityManager();

    objectFile = DummyCart.getDummyFile();
    bookProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    subscribeProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductSubscribeDto());

    objectFileRepository.save(objectFile);
    productRepository.save(bookProduct);
    productRepository.save(subscribeProduct);

    subscribe = Subscribe.builder()
        .product(subscribeProduct)
        .build();
    subscribeRepository.save(subscribe);

    bookSubscribe = BookSubscribe.builder()
        .subscribeNo(subscribe)
        .productNo(bookProduct)
        .releaseDate(LocalDate.of(2022, 10, 10))
        .build();

    bookSubscribeRepository.save(bookSubscribe);
  }

  @Disabled
  @Test
  void findBooksProductIdBySubscribeId() {

    //when
    List<Long> bookSubscribeList = bookSubscribeRepository.findBooksProductIdBySubscribeId(
        subscribe.getId());

    assertThat(bookSubscribeList.size()).isEqualTo(1);
    assertThat(bookSubscribeList.get(0)).isEqualTo(bookProduct.getId());

    em.clear();
  }
}