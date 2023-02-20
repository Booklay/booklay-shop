package com.nhnacademy.booklay.server.repository.product.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductRelation;
import com.nhnacademy.booklay.server.repository.ObjectFileRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRelationRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.List;
import javax.persistence.EntityManager;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ProductRelationRepositoryImplTest {

  @Autowired
  ProductRelationRepository productRelationRepository;
  @Autowired
  TestEntityManager entityManager;

  EntityManager em;

  Product baseProduct;
  Product targetProduct;
  ObjectFile objectFile;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ObjectFileRepository objectFileRepository;

  @BeforeEach
  void setup() {
    em = entityManager.getEntityManager();

    objectFile= DummyCart.getDummyFile();
    baseProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
//    ReflectionTestUtils.setField(baseProduct, "productId", 1L);
    targetProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductSubscribeDto());
//    ReflectionTestUtils.setField(targetProduct, "productId", 2L);

  }

  @Test()
  @Order(0)
  void findRecommendIdsByBaseProductId() {
    //given
    objectFileRepository.save(objectFile);
    baseProduct = productRepository.save(baseProduct);
    targetProduct = productRepository.save(targetProduct);

    productRelationRepository.save(
        ProductRelation.builder().relatedProduct(targetProduct).baseProduct(baseProduct).build());

    //when
    List<Long> relationProductIds =  productRelationRepository.findRecommendIdsByBaseProductId(baseProduct.getId());

    //then
    assertThat(relationProductIds.size()).isEqualTo(1);
//    assertThat(relationProductIds.get(0)).isEqualTo(targetProduct.getId());

    em.clear();
  }

  @Disabled
  @Test
  @Order(1)
  void existsByBaseAndTargetId_true() {
    objectFileRepository.save(objectFile);
    baseProduct = productRepository.save(baseProduct);
    targetProduct = productRepository.save(targetProduct);

    //when
    productRelationRepository.existsByBaseAndTargetId(baseProduct.getId(), targetProduct.getId());

    em.clear();
  }
//
//  @Test
//  void existsByBaseAndTargetId_false() {
//    productRelationRepository.existsByBaseAndTargetId(baseProduct.getId(), targetProduct.getId());
//  }
//
//  @Test
//  void deleteByBaseAndTargetId() {
//    productRelationRepository.deleteByBaseAndTargetId(baseProduct.getId(), targetProduct.getId());
//  }
}