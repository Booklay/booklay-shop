package com.nhnacademy.booklay.server.repository.product.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductRelation;
import com.nhnacademy.booklay.server.repository.product.ProductRelationRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
  @Autowired
  private ProductRepository productRepository;


  Product baseProduct;
  Product savedBaseProduct;
  Product targetProduct;
  Product savedTargetProduct;
  Product savedUnrelatedProduct;
  ObjectFile objectFile;

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
    clearRepo("product_relation", productRelationRepository);

    objectFile = DummyCart.getDummyFile();
    ObjectFile savedFile = entityManager.persist(objectFile);

    baseProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    baseProduct.setThumbnailNo(savedFile.getId());
    savedBaseProduct = productRepository.save(baseProduct);
    savedUnrelatedProduct = productRepository.save(baseProduct);

    targetProduct = DummyCart.getDummyProduct(DummyCart.getDummyProductSubscribeDto());
    targetProduct.setThumbnailNo(savedFile.getId());
    savedTargetProduct = productRepository.save(targetProduct);

    productRelationRepository.save(
        ProductRelation.builder().relatedProduct(savedTargetProduct).baseProduct(savedBaseProduct)
            .build());

  }

  @Test()
  @Order(0)
  void findRecommendIdsByBaseProductId() {

    //when
    List<Long> relationProductIds = productRelationRepository.findRecommendIdsByBaseProductId(
        savedBaseProduct.getId());

    //then
    assertThat(relationProductIds.size()).isEqualTo(1);
    assertThat(relationProductIds.get(0)).isEqualTo(targetProduct.getId());
  }

  @Test
  @Order(1)
  void existsByBaseAndTargetId_true() {
    //when
    boolean result = productRelationRepository.existsByBaseAndTargetId(savedBaseProduct.getId(),
        savedTargetProduct.getId());

    assertThat(result).isTrue();
  }

  @Test
  void existsByBaseAndTargetId_false() {

    boolean result = productRelationRepository.existsByBaseAndTargetId(savedBaseProduct.getId(),
        savedUnrelatedProduct.getId());

    assertThat(result).isFalse();
  }

  @Test
  void deleteByBaseAndTargetId() {
    productRelationRepository.deleteByBaseAndTargetId(savedBaseProduct.getId(),
        savedTargetProduct.getId());

    assertThat(productRelationRepository.existsByBaseAndTargetId(savedBaseProduct.getId(),
        savedUnrelatedProduct.getId())).isFalse();
  }
}