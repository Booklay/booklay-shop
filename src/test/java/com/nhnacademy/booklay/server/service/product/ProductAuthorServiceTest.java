package com.nhnacademy.booklay.server.service.product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.nhnacademy.booklay.server.dto.product.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductAuthor.Pk;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ProductAuthorServiceTest {

  @Autowired
  ProductAuthorService productAuthorService;
  @Autowired
  ProductService productService;
  @Autowired
  AuthorService authorService;
  @Autowired
  ProductDetailService productDetailService;

  Product product;
  ProductDetail productDetail;

  CreateProductBookRequest request = DummyCart.getDummyProductBookDto();
  Author author = DummyCart.getDummyAuthor();

  @BeforeEach
  void setup() {
    product = Product.builder()
        .price(request.getPrice())
        .pointMethod(request.isPointMethod())
        .pointRate(request.getPointRate())
        .title(request.getTitle())
        .shortDescription(request.getShortDescription())
        .longDescription(request.getLongDescription())
        .image(request.getImage())
        .isSelling(request.isSelling())
        .build();

    productService.createProduct(product);

    productDetail = ProductDetail.builder()
        .product(product)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher(request.getPublisher())
        .publishedDate(request.getPublishedDate())
        .build();

    productDetailService.createProductDetail(productDetail);
  }

  @Test
  void testProductAuthorCreate_Success() {
    Author savedAuthor = authorService.createAuthor(author);

    ProductAuthor.Pk pk = new Pk(productDetail.getId(), savedAuthor.getAuthorNo());

    ProductAuthor productAuthor = ProductAuthor.builder()
        .pk(pk)
        .productDetail(productDetail)
        .author(savedAuthor)
        .build();

    ProductAuthor expect = productAuthorService.createProductAuthor(productAuthor);

    assertThat(expect.getPk()).isEqualTo(productAuthor.getPk());
  }
}