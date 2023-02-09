package com.nhnacademy.booklay.server.service.product.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.product.request.CreateDeleteProductRelationRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.product.ProductRelationRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.product.ProductService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductRelationServiceImplTest {

  @InjectMocks
  ProductRelationServiceImpl productRelationService;

  @Mock
  ProductRelationRepository productRelationRepository;
  @Mock
  ProductService productService;
  @Mock
  ProductRepository productRepository;

  CreateDeleteProductRelationRequest request;
  CreateUpdateProductBookRequest bookRequest;
  Product baseProduct;
  Product targetProduct;
  ProductDetail productDetail;

  @BeforeEach
  void setUp() {
    request = new CreateDeleteProductRelationRequest(2L, 1L);

    bookRequest = DummyCart.getDummyProductBookDto();
    baseProduct = DummyCart.getDummyProduct(bookRequest);
    targetProduct = DummyCart.getDummyProduct(bookRequest);
    productDetail = DummyCart.getDummyProductDetail(bookRequest);
    ReflectionTestUtils.setField(baseProduct, "id", 1L);
    ReflectionTestUtils.setField(targetProduct, "id", 2L);
  }

  @Test
  void retrieveRecommendProducts_success() throws IOException {
    List<Long> recommendProductIds = new ArrayList<>();

    given(productRelationRepository.existsAllByBaseProduct_Id(baseProduct.getId())).willReturn(
        true);
    given(
        productRelationRepository.findRecommendIdsByBaseProductId(baseProduct.getId())).willReturn(
        recommendProductIds);

    productRelationService.retrieveRecommendProducts(baseProduct.getId());

    BDDMockito.then(productService).should().retrieveProductResponses(recommendProductIds);
  }

  @Test
  void retrieveRecommendProducts_failure() throws IOException {
    given(productRelationRepository.existsAllByBaseProduct_Id(baseProduct.getId())).willReturn(
        false);

    List<RetrieveProductResponse> result = productRelationService.retrieveRecommendProducts(
        baseProduct.getId());

    assertThat(result).isEqualTo(new ArrayList<>());
  }

  @Test
  void retrieveRecommendConnection() throws IOException {
    Pageable pageable = Pageable.ofSize(20);
    List<RetrieveProductResponse> content = new ArrayList<>();
    content.add(new RetrieveProductResponse(baseProduct, productDetail, new ArrayList<>()));
    Page<RetrieveProductResponse> response = new PageImpl<>(content,pageable,
        1);

    given(productService.retrieveProductPage(pageable)).willReturn(response);
    for(RetrieveProductResponse product  : response.getContent()){
      given(productRelationRepository.existsByBaseAndTargetId(baseProduct.getId(),
          product.getProductId())).willReturn(anyBoolean());
    }

    productRelationService.retrieveRecommendConnection(baseProduct.getId(), pageable);
  }

  @Test
  void createProductRelation() {

    given(productRepository.findById(request.getBaseId())).willReturn(
        Optional.ofNullable(baseProduct));
    given(productRepository.findById(request.getTargetId())).willReturn(
        Optional.ofNullable(targetProduct));

    productRelationService.createProductRelation(request);

    BDDMockito.then(productRelationRepository).should().save(any());
  }

  @Test
  void deleteProductRelation() {
    given(productRepository.findById(request.getBaseId())).willReturn(
        Optional.ofNullable(baseProduct));
    given(productRepository.findById(request.getTargetId())).willReturn(
        Optional.ofNullable(targetProduct));

    productRelationService.deleteProductRelation(request);

    BDDMockito.then(productRelationRepository).should()
        .deleteByBaseAndTargetId(baseProduct.getId(), targetProduct.getId());
  }
}