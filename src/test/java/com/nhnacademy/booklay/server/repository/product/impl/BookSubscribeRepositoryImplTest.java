package com.nhnacademy.booklay.server.repository.product.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.BookSubscribe;
import com.nhnacademy.booklay.server.entity.BookSubscribe.Pk;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.repository.product.BookSubscribeRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class BookSubscribeRepositoryImplTest {

  @Autowired
  BookSubscribeRepository bookSubscribeRepository;
  @Autowired
  TestEntityManager entityManager;

  Product bookProduct;
  Product subscribeProduct;
  Subscribe subscribe;
  ObjectFile objectFile;
  BookSubscribe bookSubscribe;

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private SubscribeRepository subscribeRepository;

  void clearRepo(String entityName, JpaRepository jpaRepository) {
    jpaRepository.deleteAll();

    String query =
        String.format("ALTER TABLE `%s` ALTER COLUMN product_no RESTART WITH 1",
            entityName, entityName);

    this.entityManager
        .getEntityManager()
        .createNativeQuery(query)
        .executeUpdate();
  }

  @BeforeEach
  void setUp() {
    clearRepo("subscribe", subscribeRepository);
    clearRepo("product", productRepository);
//    clearRepo("book_subscribe", bookSubscribeRepository);

    objectFile = DummyCart.getDummyFile();
    ObjectFile savedFile = entityManager.persist(objectFile);

    bookProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    bookProduct.setThumbnailNo(savedFile.getId());
    subscribeProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductSubscribeDto());
    subscribeProduct.setThumbnailNo(savedFile.getId());

    productRepository.save(bookProduct);
    Product savedSubscribeProduct = productRepository.save(subscribeProduct);

    subscribe = Subscribe.builder()
        .product(subscribeProduct)
        .build();
    subscribe.setProduct(savedSubscribeProduct);
    subscribeRepository.save(subscribe);

    bookSubscribe = BookSubscribe.builder()
        .subscribeNo(subscribe)
        .productNo(bookProduct)
        .releaseDate(LocalDate.of(2022, 10, 10))
        .pk(new Pk(subscribe.getId(), bookProduct.getId()))
        .build();
    bookSubscribeRepository.save(bookSubscribe);
  }

  @Test
  void findBooksProductIdBySubscribeId() {

    //when
    List<Long> bookSubscribeList = bookSubscribeRepository.findBooksProductIdBySubscribeId(
        subscribe.getId());

    assertThat(bookSubscribeList.size()).isEqualTo(1);
    assertThat(bookSubscribeList.get(0)).isEqualTo(bookProduct.getId());
  }
}