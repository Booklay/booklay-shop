package com.nhnacademy.booklay.server.repository.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Authority;
import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  ProductRepository productRepository;

  @Test
  @DisplayName("AuthorityRepository save test")
  void testProductSave() {
    //given
    CreateUpdateProductBookRequest request = DummyCart.getDummyProductBookDto();
    Product product = DummyCart.getDummyProduct(request);

    //when
    entityManager.persist(product.getImage());
    Product expected = productRepository.save(product);

    //then
    assertThat(expected.isDeleted()).isEqualTo(true);

  }
}