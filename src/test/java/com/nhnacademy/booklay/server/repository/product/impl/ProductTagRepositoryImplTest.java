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
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ProductTagRepositoryImplTest {

  @Autowired
  ProductTagRepository productTagRepository;
  @Autowired
  TestEntityManager entityManager;

  EntityManager em;
  Product product;
  Tag tag;

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private TagRepository tagRepository;
  @Autowired
  private ObjectFileRepository objectFileRepository;

  @BeforeEach
  void setUp() {
    em = entityManager.getEntityManager();

    objectFileRepository.save(DummyCart.getDummyFile());
    product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    productRepository.save(product);

    tag = new Tag("test tag");
    tagRepository.save(tag);

    ProductTag productTag = ProductTag.builder().product(product).tag(tag)
        .pk(new Pk(product.getId(), tag.getId())).build();
    productTagRepository.save(productTag);

  }

  @Test
  void findTagsByProductId() {

    //when
    List<RetrieveTagResponse> tagResponses = productTagRepository.findTagsByProductId(
        product.getId());

    //then
    assertThat(tagResponses).hasSize(1);
    assertThat(tagResponses.get(0).getName()).isEqualTo(tag.getName());

    em.clear();
  }
}