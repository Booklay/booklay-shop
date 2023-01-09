package com.nhnacademy.booklay.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.product.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.service.ImageService;
import com.nhnacademy.booklay.server.service.product.impl.ProductDetailServiceImpl;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProductDetailServiceTest {

  @InjectMocks
  ProductDetailServiceImpl productDetailService;

  @Mock
  ProductDetailRepository productDetailRepository;

  ProductDetail productDetail;
  Product product;
  CreateProductBookRequest request = DummyCart.getDummyProductBookDto();

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
    productDetail.setId(1L);
  }

  @Test
  void testProductDetailCreate_Success() {
    when(productDetailRepository.save(productDetail)).thenReturn(productDetail);
    ProductDetail expect = productDetailService.createProductDetail(productDetail);

    assertThat(expect.getPage()).isEqualTo(productDetail.getPage());
    assertThat(expect.getProduct().getId()).isEqualTo(productDetail.getProduct().getId());
  }

  @Test
  void testProductDetailUpdate_Success() {
    when(productDetailRepository.save(productDetail)).thenReturn(productDetail);
    ProductDetail original = productDetailService.createProductDetail(productDetail);

    log.info("출력 : " + original.getId());

    ProductDetail updateSource = ProductDetail.builder()
        .product(product)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher("이건 진짜 증복 안될만한 출판사명")
        .publishedDate(request.getPublishedDate())
        .build();

    updateSource.setId(original.getId());

    log.info("출력 : " + updateSource.getId());

    when(productDetailRepository.save(updateSource)).thenReturn(updateSource);
    productDetailService.updateProductDetail(updateSource);

    when(productDetailRepository.findById(updateSource.getId())).thenReturn(Optional.ofNullable(updateSource));
    ProductDetail updated = productDetailService.retrieveById(original.getId());

    assertThat(updated.getId()).isEqualTo(original.getId());
    assertThat(updated.getPublisher()).isNotEqualTo(original.getPublisher());
  }

}