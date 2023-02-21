package com.nhnacademy.booklay.server.repository.product.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductAuthor.Pk;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
//@Transactional
class ProductDetailRepositoryImplTest {

  @Autowired
  ProductDetailRepository productDetailRepository;

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  private ProductRepository productRepository;

  Product product;
  ObjectFile objectFile;
  CreateUpdateProductBookRequest request;
  Author savedAuthor;
  @Autowired
  private ProductAuthorRepository productAuthorRepository;
  @Autowired
  private AuthorRepository authorRepository;


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
    ObjectFile savedFile = entityManager.persist(objectFile);

    product = DummyCart.getDummyProduct(DummyCart.getDummyProductBookDto());
    product.setThumbnailNo(savedFile.getId());
    Product savedProduct = productRepository.save(product);

    ProductDetail productDetail = ProductDetail.builder()
        .product(savedProduct)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher(request.getPublisher())
        .publishedDate(request.getPublishedDate())
        .build();

    ProductDetail savedProductDetail = productDetailRepository.save(productDetail);

    savedAuthor = authorRepository.save(DummyCart.getDummyAuthor());

    ProductAuthor productAuthor = ProductAuthor.builder().productDetail(savedProductDetail)
        .author(savedAuthor)
        .pk(new Pk(savedProductDetail.getId(), savedAuthor.getAuthorId()))
        .build();
    productAuthorRepository.save(productAuthor);

  }

  @Test
  void findAuthorsByProductDetailId() {

    //when
    List<RetrieveAuthorResponse> result = productDetailRepository.findAuthorsByProductDetailId(1L);

    //then
    assertThat(result).size().isEqualTo(1);
    assertThat(result.get(0).getName()).isEqualTo(savedAuthor.getName());

  }

}