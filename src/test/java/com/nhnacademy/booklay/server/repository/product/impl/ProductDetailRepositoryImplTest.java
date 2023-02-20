package com.nhnacademy.booklay.server.repository.product.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.ObjectFileRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
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
  @Autowired
  private ProductAuthorRepository productAuthorRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ObjectFileRepository objectFileRepository;

  @BeforeEach
  void SetUp() {
    em = entityManager.getEntityManager();

  }

  @Test
  void findAuthorsByProductDetailId() {
    entityManager.clear();

    //given
//    RetrieveAuthorResponse authorResponse = new RetrieveAuthorResponse(DummyCart.getDummyAuthor());
//    List<RetrieveAuthorResponse> authorResponseList = new ArrayList<>();
//    authorResponseList.add(authorResponse);
    ProductAuthor productAuthor = DummyCart.getDummyProductAuthor(
        DummyCart.getDummyProductBookDto());
    objectFileRepository.save(DummyCart.getDummyFile());
    Product product = productRepository.save(
        DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto()));
    productDetail = DummyCart.getDummyProductDetail(DummyCart.getDummyProductBookDto());
//    productDetailRepository.save(productDetail);
//    productAuthorRepository.save(productAuthor);

    //when
    List<RetrieveAuthorResponse> result = productDetailRepository.findAuthorsByProductDetailId(1L);

    //then
    assertThat(result).size().isEqualTo(0);
  }

}