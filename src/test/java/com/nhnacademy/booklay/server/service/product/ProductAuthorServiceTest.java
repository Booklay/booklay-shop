package com.nhnacademy.booklay.server.service.product;

import com.nhnacademy.booklay.server.dto.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductAuthor.Pk;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.service.product.impl.ProductAuthorServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ProductAuthorServiceTest {

  @InjectMocks
  ProductAuthorServiceImpl productAuthorService;
  @Mock
  ProductAuthorRepository productAuthorRepository;

  Product product;
  ProductDetail productDetail;

  CreateProductBookRequest request = DummyCart.getDummyProductBookDto();
  Author author = DummyCart.getDummyAuthor();

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
  void testProductAuthorCreate_Success() {
    //given
    author.setAuthorNo(1L);

    ProductAuthor.Pk pk = new Pk(productDetail.getId(), author.getAuthorNo());

    ProductAuthor productAuthor = ProductAuthor.builder()
        .pk(pk)
        .productDetail(productDetail)
        .author(author)
        .build();

    //when
    when(productAuthorRepository.save(productAuthor)).thenReturn(productAuthor);

    ProductAuthor expect = productAuthorService.createProductAuthor(productAuthor);

    assertThat(expect.getPk()).isEqualTo(productAuthor.getPk());
  }
}