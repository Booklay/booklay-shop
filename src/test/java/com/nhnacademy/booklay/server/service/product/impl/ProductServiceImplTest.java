package com.nhnacademy.booklay.server.service.product.impl;


import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductBookResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.repository.ImageRepository;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.repository.product.CategoryProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductTagRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.storage.impl.FileServiceImpl;
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
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

  @InjectMocks
  private ProductServiceImpl productService;

  @Mock
  private ProductRepository productRepository;
  @Mock
  private CategoryRepository categoryRepository;
  @Mock
  private ProductDetailRepository productDetailRepository;
  @Mock
  private CategoryProductRepository categoryProductRepository;
  @Mock
  private AuthorRepository authorRepository;
  @Mock
  private ProductAuthorRepository productAuthorRepository;
  @Mock
  private SubscribeRepository subscribeRepository;
  @Mock
  private ProductTagRepository productTagRepository;
  @Mock
  private FileServiceImpl fileService;

  Product productBook;
  ProductDetail productDetail;
  CreateUpdateProductBookRequest productRequest;
  Product productSubscribe;
  Subscribe subscribe;
  CreateUpdateProductSubscribeRequest subscribeRequest;


  @BeforeEach
  void setUp() {
    productRequest = DummyCart.getDummyProductBookDto();
    productBook = DummyCart.getDummyProduct(productRequest);
    productDetail = DummyCart.getDummyProductDetail(productRequest);

    subscribeRequest = DummyCart.getDummyProductSubscribeDto();
    productSubscribe = DummyCart.getDummyProduct(subscribeRequest);
    subscribe = DummyCart.getDummySubscribe(subscribeRequest);
  }

  @Test
  void createBookProduct_success() throws Exception {
    ReflectionTestUtils.setField(productBook, "id", 1L);
    ReflectionTestUtils.setField(productDetail, "id", 1L);

    given(productRepository.save(any())).willReturn(productBook);
    for (int i = 0; i < productRequest.getCategoryIds().size(); i++) {
      given(categoryRepository.findById(productRequest.getCategoryIds().get(i))).willReturn(
          Optional.ofNullable(DummyCart.getDummyCategory()));
    }

    given(productDetailRepository.save(any())).willReturn(productDetail);
    for (int i = 0; i < productRequest.getAuthorIds().size(); i++) {
      given(authorRepository.findById(productRequest.getAuthorIds().get(i))).willReturn(
          Optional.of(DummyCart.getDummyAuthor()));
    }

    //when
    Long result = productService.createBookProduct(productRequest);

    //then
    assertThat(result).isEqualTo(productBook.getId());
  }

  @Test
  void createSubscribeProduct_success() throws Exception {
    ReflectionTestUtils.setField(productSubscribe, "id", 1L);
    ReflectionTestUtils.setField(subscribe, "id", 1L);

    given(productRepository.save(any())).willReturn(productSubscribe);
    for (int i = 0; i < subscribeRequest.getCategoryIds().size(); i++) {
      given(categoryRepository.findById(subscribeRequest.getCategoryIds().get(i))).willReturn(
          Optional.ofNullable(DummyCart.getDummyCategory()));
    }

    given(subscribeRepository.save(any())).willReturn(subscribe);

    //when
    Long result = productService.createSubscribeProduct(subscribeRequest);

    //then
    assertThat(result).isEqualTo(productSubscribe.getId());
  }


//  @Test
  void testRetrieveBookData_success(){
    Long targetId = 1L;
    RetrieveProductBookResponse response = new RetrieveProductBookResponse();
    List<Long> authorIds = new ArrayList<>();
    authorIds.add(1L);
    List<Long> categoryIds = new ArrayList<>();
    categoryIds.add(1L);

    given(productRepository.findProductBookDataByProductId(targetId)).willReturn(response);
    given(productDetailRepository.findAuthorIdsByProductDetailId(response.getProductDetailId())).willReturn(authorIds);
    given(productRepository.findCategoryIdsByProductId(response.getProductId())).willReturn(categoryIds);

    RetrieveProductBookResponse result = productService.retrieveBookData(targetId);

//    assertThat(result.getCategoryIds()).isEqualTo(categoryIds);
//    assertThat(result.getAuthorIds()).isEqualTo(authorIds);
  }

//  @Test
  void testRetrieveSubscribeData(){
    //given
    Long targetId = 1L;
    RetrieveProductSubscribeResponse response = new RetrieveProductSubscribeResponse();
    List<Long> categoryIds = new ArrayList<>();
    categoryIds.add(1L);

    given(productRepository.findProductSubscribeDataByProductId(targetId)).willReturn(response);
    given(productRepository.findCategoryIdsByProductId(response.getProductId())).willReturn(categoryIds);

    //when
    RetrieveProductSubscribeResponse result = productService.retrieveSubscribeData(targetId);

    //then
//    assertThat(result.getCategoryIds()).isEqualTo(categoryIds);
  }

  @Test
  void testSoftDelete_success(){
    //given
    given(productRepository.findById(productBook.getId())).willReturn(
        Optional.ofNullable(productBook));
    assertThat(productBook.isDeleted()).isFalse();

    //when
    productService.softDelete(productBook.getId());

    //then
    assertThat(productBook.isDeleted()).isTrue();
  }

}
