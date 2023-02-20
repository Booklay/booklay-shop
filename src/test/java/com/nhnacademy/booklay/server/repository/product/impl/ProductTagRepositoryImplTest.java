package com.nhnacademy.booklay.server.repository.product.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductTag;
import com.nhnacademy.booklay.server.entity.ProductTag.Pk;
import com.nhnacademy.booklay.server.entity.Tag;
import com.nhnacademy.booklay.server.repository.ObjectFileRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductTagRepository;
import com.nhnacademy.booklay.server.repository.product.TagRepository;
import java.util.List;
import javax.persistence.EntityManager;
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
class ProductTagRepositoryImplTest {

  @Autowired
  ProductTagRepository productTagRepository;
  @Autowired
  TestEntityManager entityManager;

  Product product;
  Product savedProduct;
  Tag tag;

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private TagRepository tagRepository;
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
  void setUp() {
    clearRepo("product", productRepository);

    objectFileRepository.save(DummyCart.getDummyFile());
    product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    savedProduct = entityManager.persist(product);

    tag = new Tag("test tag");
    Tag savedTag = entityManager.persist(tag);

    ProductTag productTag = ProductTag.builder().product(savedProduct).tag(savedTag)
        .pk(new Pk(savedProduct.getId(), savedTag.getId())).build();
    productTagRepository.save(productTag);
  }

  @Test
  void findTagsByProductId() {

    //when
    List<RetrieveTagResponse> tagResponses = productTagRepository.findTagsByProductId(
        savedProduct.getId());

    //then
    assertThat(tagResponses).hasSize(1);
    assertThat(tagResponses.get(0).getName()).isEqualTo(tag.getName());
  }
}