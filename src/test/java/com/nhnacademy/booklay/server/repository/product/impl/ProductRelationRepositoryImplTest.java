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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class ProductRelationRepositoryImplTest {

  @Autowired
  ProductRelationRepository productRelationRepository;
  @Autowired
  TestEntityManager entityManager;

  EntityManager em;

  Product baseProduct;
  Product savedBaseProduct;
  Product targetProduct;
  Product savedTargetProduct;
  ObjectFile objectFile;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ObjectFileRepository objectFileRepository;

  void clearRepo(String entityName, JpaRepository jpaRepository) {
    jpaRepository.deleteAll();

    String query =
        String.format("ALTER TABLE `%s` ALTER COLUMN `%s_no` RESTART WITH 1", entityName,
            entityName);

    this.entityManager
        .getEntityManager()
        .createNativeQuery(query)
        .executeUpdate();
  }

  @BeforeEach
  void setup() {
    em = entityManager.getEntityManager();
    clearRepo("product_relation", productRelationRepository);

    objectFile= DummyCart.getDummyFile();
    ObjectFile savedFile = entityManager.persist(objectFile);

    baseProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    baseProduct.setThumbnailNo(savedFile.getId());
    savedBaseProduct = productRepository.save(baseProduct);

    targetProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductSubscribeDto());
    targetProduct.setThumbnailNo(savedFile.getId());
    savedTargetProduct = productRepository.save(targetProduct);

    productRelationRepository.save(
        ProductRelation.builder().relatedProduct(savedTargetProduct).baseProduct(savedBaseProduct).build());

  }

  @Test()
  @Order(0)
  void findRecommendIdsByBaseProductId() {
    //given



    //when
    List<Long> relationProductIds =  productRelationRepository.findRecommendIdsByBaseProductId(savedBaseProduct.getId());

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

    em.clear();

    //when
    productRelationRepository.existsByBaseAndTargetId(baseProduct.getId(), targetProduct.getId());

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