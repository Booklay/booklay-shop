package com.nhnacademy.booklay.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.booklay.server.dto.product.ProductBookDto;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ProductServiceTest {

  @Autowired
  ProductService productService;
  @Autowired
  ImageService imageService;

  Product product;
  ProductBookDto request = DummyCart.getDummyProductBookDto();

  @BeforeEach
  void setup(){
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
  }

  @Test
  void testProductCreate_success(){
    imageService.createImage(product.getImage());
    Product expect = productService.createProduct(product);

    assertThat(expect.getTitle()).isEqualTo(product.getTitle());
  }

  @Test
  void testProductUpdate_Success(){
    imageService.createImage(product.getImage());
    Product original = productService.createProduct(product);


    Product updateSource = Product.builder()
        .price(30000L)
        .pointMethod(request.isPointMethod())
        .pointRate(request.getPointRate())
        .title(request.getTitle())
        .shortDescription(request.getShortDescription())
        .longDescription(request.getLongDescription())
        .image(request.getImage())
        .isSelling(request.isSelling())
        .build();


    Product updated = productService.updateProduct(original.getId(), updateSource);


    assertThat(updated.getId()).isEqualTo(original.getId());
    assertThat(updated.getPrice()).isNotEqualTo(original.getPrice());
  }

}