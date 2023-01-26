package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.repository.product.CategoryProductRepository;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
import com.nhnacademy.booklay.server.repository.product.*;
import com.nhnacademy.booklay.server.service.product.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    CreateUpdateProductBookRequest request = DummyCart.getDummyProductBookDto();

    @BeforeEach
    void setup() {
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