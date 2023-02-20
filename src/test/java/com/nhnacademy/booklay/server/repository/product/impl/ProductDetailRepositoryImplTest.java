package com.nhnacademy.booklay.server.repository.product.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.ObjectFileRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.List;
import org.aspectj.util.Reflection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
//@Transactional
class ProductDetailRepositoryImplTest {

  @Autowired
  ProductDetailRepository productDetailRepository;

  @Autowired
  TestEntityManager entityManager;

  ProductDetail productDetail;
  @Autowired
  private ProductAuthorRepository productAuthorRepository;
  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private ObjectFileRepository objectFileRepository;

  Product product;
  ObjectFile objectFile;
  CreateUpdateProductBookRequest request;


  void clearRepo(String entityName, JpaRepository jpaRepository) {
    jpaRepository.deleteAll();

    String query =
        String.format("ALTER TABLE `%s` ALTER COLUMN book_no RESTART WITH 1", entityName,
            entityName);

    this.entityManager
        .getEntityManager()
        .createNativeQuery(query)
        .executeUpdate();
  }

  @BeforeEach
  void SetUp() {
    clearRepo("product_detail", productDetailRepository);

    request = DummyCart.getDummyProductBookDto();

    objectFile = DummyCart.getDummyFile();
    entityManager.persist(objectFile);

    product =DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    Product savedProduct = entityManager.persist(product);

    ProductDetail productDetail = ProductDetail.builder()
        .product(savedProduct)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher(request.getPublisher())
        .publishedDate(request.getPublishedDate())
        .build();

    productDetailRepository.save(productDetail);

  }

  @Test
  void findAuthorsByProductDetailId() {

    //given
    ProductAuthor productAuthor = DummyCart.getDummyProductAuthor(DummyCart.getDummyProductBookDto());

    //when
    List<RetrieveAuthorResponse> result = productDetailRepository.findAuthorsByProductDetailId(1L);

    //then
    assertThat(result).size().isEqualTo(0);

  }

}