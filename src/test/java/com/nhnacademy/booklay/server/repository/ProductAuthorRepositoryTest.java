package com.nhnacademy.booklay.server.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class ProductAuthorRepositoryTest {

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  ProductAuthorRepository productAuthorRepository;
  @Autowired
  ProductRepository productRepository;
  @Autowired
  AuthorRepository authorRepository;
  @Autowired
  ProductDetailRepository productDetailRepository;


  @Test
  void testProductAuthorSave(){
    ProductAuthor productAuthor = DummyCart.getDummyProductAuthor();

    entityManager.persist(productAuthor.getProductDetail().getProduct().getImage());
    productRepository.save(productAuthor.getProductDetail().getProduct());
    productDetailRepository.save(productAuthor.getProductDetail());

    authorRepository.save(productAuthor.getAuthor());

    ProductAuthor expect = productAuthorRepository.save(productAuthor);

    assertThat(expect.getAuthor().getName()).isEqualTo(productAuthor.getAuthor().getName());
  }

  @Test
  void testProductAuthorFind(){
    ProductAuthor productAuthor = DummyCart.getDummyProductAuthor();

    entityManager.persist(productAuthor.getProductDetail().getProduct().getImage());
    productRepository.save(productAuthor.getProductDetail().getProduct());
    productDetailRepository.save(productAuthor.getProductDetail());

    authorRepository.save(productAuthor.getAuthor());

    productAuthorRepository.save(productAuthor);

    ProductAuthor expect = productAuthorRepository.findById(productAuthor.getPk()).orElseThrow(() -> new IllegalArgumentException("no"));

    assertThat(expect.getAuthor().getName()).isEqualTo(productAuthor.getAuthor().getName());
  }
}