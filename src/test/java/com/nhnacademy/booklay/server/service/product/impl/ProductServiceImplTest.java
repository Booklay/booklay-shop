package com.nhnacademy.booklay.server.service.product.impl;


import com.nhnacademy.booklay.server.repository.CategoryRepository;
import com.nhnacademy.booklay.server.repository.ImageRepository;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.repository.product.CategoryProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductTagRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
  private ImageRepository imageRepository;
  @Mock
  private ProductTagRepository productTagRepository;
}
