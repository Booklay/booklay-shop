package com.nhnacademy.booklay.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.booklay.server.dto.product.ProductBookDto;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
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
    log.info("출력 섬네일 : " + product.getImage().getId() +"   " + product.getImage().getExt()+ "   " +product.getImage().getAddress() );

  //TODO : 연관관계 매핑이 된 thumbnail_no 가 없다고 나오는데, Image repo에 image 추가하는거 같이 작성해줘야 하는것 같다.-> 일단 미룸...
    Product expect = productService.createProduct(product);

    assertThat(expect.getTitle()).isEqualTo(product.getTitle());
  }
}