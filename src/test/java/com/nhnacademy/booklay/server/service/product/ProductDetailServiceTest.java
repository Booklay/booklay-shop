package com.nhnacademy.booklay.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.product.ProductBookDto;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ProductDetailServiceTest {

  @Autowired
  ProductDetailService productDetailService;

  @Autowired
  ProductService productService;
  @Autowired
  ImageService imageService;

  ProductDetail productDetail;
  Product product;
  ProductBookDto request = DummyCart.getDummyProductBookDto();

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

    productDetail = ProductDetail.builder()
        .product(product)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher(request.getPublisher())
        .publishedDate(request.getPublishedDate())
        .build();
  }

  @Test
  void testProductDetailCreate_Success() {
    //TODO : Mock 사용법 알아내서 수정할것
    imageService.createImage(product.getImage());
    productService.createProduct(product);

    ProductDetail expect = productDetailService.createProductDetail(productDetail);

    assertThat(expect.getPage()).isEqualTo(productDetail.getPage());
    assertThat(expect.getProduct().getId()).isEqualTo(productDetail.getProduct().getId());
  }

  @Test
  void testProductDetailUpdate_Success() {
    imageService.createImage(product.getImage());
    productService.createProduct(product);

    ProductDetail original = productDetailService.createProductDetail(productDetail);

    ProductDetail updateSource = ProductDetail.builder()
        .product(product)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher("이건 진짜 증복 안될만한 출판사명")
        .publishedDate(request.getPublishedDate())
        .build();

    ProductDetail updated = productDetailService.updateProductDetail(original.getId(), updateSource);

    assertThat(updated.getId()).isEqualTo(original.getId());
    assertThat(updated.getPublisher()).isNotEqualTo(original.getPublisher());
  }

}