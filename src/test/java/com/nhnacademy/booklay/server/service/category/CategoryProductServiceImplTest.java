package com.nhnacademy.booklay.server.service.category;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.repository.product.CategoryProductRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @Author: 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class CategoryProductServiceImplTest {

  @InjectMocks
  CategoryProductServiceImpl categoryProductService;
  @Mock
  CategoryProductRepository categoryProductRepository;

  Long productId;
  CategoryProduct categoryProduct;
  List<CategoryProduct> categoryProducts;

  @BeforeEach
  void setup() {
    productId = 1L;

    categoryProduct = DummyCart.getDummyCategoryProduct(DummyCart.getDummyProductBookDto());

    categoryProducts = new ArrayList<>();
    categoryProducts.add(categoryProduct);
  }


  @Test
  void retrieveCategoryIdListByProductId() {
    given(categoryProductRepository.findAllByPk_ProductId(productId)).willReturn(categoryProducts);

    List<Long> returnList = new ArrayList<>();
    for (CategoryProduct categoryProduct : categoryProducts) {
      returnList.add(categoryProduct.getPk().getCategoryId());
    }

    //when
    List<Long> actual = categoryProductService.retrieveCategoryIdListByProductId(productId);

    //then
    assertThat(actual.get(0)).isEqualTo(returnList.get(0));
    assertThat(actual).hasSameSizeAs(returnList);
  }

  @Test
  void retrieveCategoryIdListMultiValueMapByProductIdList() {
    List<Long> productIdList = new ArrayList<>();
    productIdList.add(productId);

    MultiValueMap<Long, Long> returnMap = new LinkedMultiValueMap<>();

    given(categoryProductRepository.findAllByPk_ProductIdIn(productIdList)).willReturn(
        categoryProducts);

    for (CategoryProduct categoryProduct : categoryProducts) {
      returnMap.add(categoryProduct.getPk().getProductId(), categoryProduct.getPk()
          .getCategoryId());
    }

    //when
    MultiValueMap<Long, Long> actual = categoryProductService.retrieveCategoryIdListMultiValueMapByProductIdList(
        productIdList);

    //then
    assertThat(actual).hasSameSizeAs(returnMap);
  }
}