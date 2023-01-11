package com.nhnacademy.booklay.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nhnacademy.booklay.server.dto.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.CategoryProductRepository;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.ImageService;
import com.nhnacademy.booklay.server.service.product.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProductServiceTest {

  @InjectMocks
  ProductServiceImpl productService;
  @Mock
  ProductRepository productRepository;
  @Mock
  CategoryRepository categoryRepository;
  @Mock
  ProductDetailRepository productDetailRepository;
  @Mock
  CategoryProductRepository categoryProductRepository;
  @Mock
  AuthorRepository authorRepository;
  @Mock
  ProductAuthorRepository productAuthorRepository;
  @Mock
  SubscribeRepository subscribeRepository;

  CreateProductBookRequest request = DummyCart.getDummyProductBookDto();
  @BeforeEach
  void setup(){
  }

//  @Test
//  void testProductBookCreate_Success() throws Exception {
//    Product product = DummyCart.getDummyProduct(request);
//    ProductAuthor productAuthor = DummyCart.getDummyProductAuthor(request);
//    ProductDetail productDetail = DummyCart.getDummyProductDetail(request);
//    CategoryProduct categoryProduct = DummyCart.getDummyCategoryProduct(request);
//
//    when(productRepository.save(product)).thenReturn(product);
//
//    when(productService.createBookProduct(request));
//
//    BDDMockito.then(productRepository).should().save(product);
//    BDDMockito.then(productAuthorRepository).should().save(productAuthor);
//    BDDMockito.then(productDetailRepository).should().save(productDetail);
//    BDDMockito.then(categoryProductRepository).should().save(categoryProduct);
//  }

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