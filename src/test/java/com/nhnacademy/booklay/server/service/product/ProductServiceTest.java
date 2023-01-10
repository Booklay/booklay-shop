package com.nhnacademy.booklay.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;

import com.nhnacademy.booklay.server.dto.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class ProductServiceTest {

  @Autowired
  ProductService productService;
  @Autowired
  ImageService imageService;

  Product product;
  CreateProductBookRequest request = DummyCart.getDummyProductBookDto();

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
//
//  @Test
//  void testProductBookCreate_Success(){
//
//  }
//
//  @Test
//  void testProductSubscribeCreate_Success(){
//
//  }
//
//  @Test
//  void testProductBookUpdate_Success(){
//
//  }
//
//  @Test
//  void testProductSubscribeUpdate_Success(){
//
//  }

}