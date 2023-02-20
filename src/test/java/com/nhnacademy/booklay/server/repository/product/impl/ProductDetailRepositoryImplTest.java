package com.nhnacademy.booklay.server.repository.product.impl;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ProductDetailRepositoryImplTest {

  @Autowired
  ProductDetailRepository productDetailRepository;

  @Autowired
  TestEntityManager entityManager;

  EntityManager em;
  ProductDetail productDetail;

  @BeforeEach
  void SetUp() {
    em = entityManager.getEntityManager();
    productDetail = DummyCart.getDummyProductDetail(DummyCart.getDummyProductBookDto());
  }

  @Test
  void findAuthorsByProductDetailId() {
    entityManager.clear();
    List<RetrieveAuthorResponse> authorResponses = productDetailRepository.findAuthorsByProductDetailId(1L);


  }

}