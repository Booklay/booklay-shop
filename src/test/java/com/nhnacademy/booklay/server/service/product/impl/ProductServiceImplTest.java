package com.nhnacademy.booklay.server.service.product.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.nhnacademy.booklay.server.dto.cart.CartDto;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.search.request.SearchIdRequest;
import com.nhnacademy.booklay.server.dummy.Dummy;
import com.nhnacademy.booklay.server.dummy.DummyCart;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.BookSubscribe;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.entity.CategoryProduct.Pk;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.exception.service.NotEnoughStockException;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.repository.product.BookSubscribeRepository;
import com.nhnacademy.booklay.server.repository.product.CategoryProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductTagRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.RedisCacheService;
import com.nhnacademy.booklay.server.service.storage.impl.FileServiceImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author 최규태
 */

@Slf4j
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @InjectMocks
  private ProductServiceImpl productService;
  @Mock
  RedisCacheService redisCacheService;

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
  @Mock
  private BookSubscribeRepository bookSubscribeRepository;

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

    ReflectionTestUtils.setField(productBook, "id", 1L);
    ReflectionTestUtils.setField(productDetail, "id", 1L);
  }

  @Test
  void createBookProduct_success() throws Exception {

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


  @Test
  void retrieveBookData_success() {
    Long targetId = 1L;

    //when
    productService.retrieveBookData(targetId);

    BDDMockito.then(productRepository).should().retrieveProductById(targetId);

  }


  @Test
  void updateBookProduct() throws Exception {
    ReflectionTestUtils.setField(productBook, "id", 1L);
    ReflectionTestUtils.setField(productDetail, "id", 1L);

    given(productRepository.existsById(productRequest.getProductId())).willReturn(true);

    productBook.setId(productRequest.getProductId());
    productBook.setCreatedAt(productRequest.getCreatedAt());
    given(productRepository.save(any())).willReturn(productBook);

//    BDDMockito.then(categoryProductRepository).should().deleteAllByProductId(productBook.getId());

    for (int i = 0; i < productRequest.getCategoryIds().size(); i++) {
      CategoryProduct.Pk pk = new Pk(productBook.getId(), productRequest.getCategoryIds().get(i));
      Category dummyCategory = Dummy.getDummyCategory();
      given(categoryRepository.findById(productRequest.getCategoryIds().get(i))).willReturn(
          Optional.ofNullable(dummyCategory));

      CategoryProduct categoryProduct = CategoryProduct.builder()
          .pk(pk)
          .product(productBook)
          .category(dummyCategory)
          .build();

      given(categoryProductRepository.save(any())).willReturn(categoryProduct);
    }

    // product detail
    productDetail.setId(productRequest.getProductDetailId());

    if (Objects.nonNull(productRequest.getStorage())) {
      productDetail.setStorage(productRequest.getStorage());
    }
    if (Objects.nonNull(productRequest.getEbookAddress())) {
      productDetail.setEbookAddress(productRequest.getEbookAddress());
    }

    given(productDetailRepository.existsById(productDetail.getId())).willReturn(true);
    given(productDetailRepository.save(any())).willReturn(productDetail);

//    BDDMockito.then(productAuthorRepository).should().deleteAllByProductDetailId(productDetail.getId());

    for (int i = 0; i < productRequest.getAuthorIds().size(); i++) {
      Author dummyAuthor = DummyCart.getDummyAuthor();
      given(authorRepository.findById(productRequest.getAuthorIds().get(i))).willReturn(
          Optional.of(dummyAuthor));

      ProductAuthor.Pk pk = new ProductAuthor.Pk(productDetail.getId(), dummyAuthor.getAuthorId());

      ProductAuthor productAuthor = ProductAuthor.builder()
          .pk(pk)
          .author(dummyAuthor)
          .productDetail(productDetail)
          .build();

      given(productAuthorRepository.save(any())).willReturn(productAuthor);
    }

    productService.updateBookProduct(productRequest);

    BDDMockito.then(productRepository).should().save(any());
  }

  @Test
  void updateSubscribeProduct_success() throws IOException {

    given(productRepository.existsById(subscribeRequest.getProductId())).willReturn(true);

    productSubscribe.setId(subscribeRequest.getProductId());
    productSubscribe.setCreatedAt(subscribeRequest.getCreatedAt());
    given(productRepository.save(any())).willReturn(productSubscribe);

    for (int i = 0; i < productRequest.getCategoryIds().size(); i++) {
      CategoryProduct.Pk pk = new Pk(productBook.getId(), productRequest.getCategoryIds().get(i));
      Category dummyCategory = Dummy.getDummyCategory();
      given(categoryRepository.findById(productRequest.getCategoryIds().get(i))).willReturn(
          Optional.ofNullable(dummyCategory));

      CategoryProduct categoryProduct = CategoryProduct.builder()
          .pk(pk)
          .product(productBook)
          .category(dummyCategory)
          .build();

      given(categoryProductRepository.save(any())).willReturn(categoryProduct);
    }

    if (Objects.nonNull(subscribeRequest.getPublisher())) {
      subscribe.setPublisher(subscribeRequest.getPublisher());
    }

    given(subscribeRepository.existsById(subscribe.getId())).willReturn(true);
    given(subscribeRepository.save(any())).willReturn(subscribe);

    productService.updateSubscribeProduct(subscribeRequest);

    BDDMockito.then(productRepository).should().save(any());
  }

  @Test
  void testRetrieveProductPage_success() throws IOException {
    List<RetrieveProductResponse> finalContent = new ArrayList<>();
    finalContent.add(new RetrieveProductResponse(productBook, subscribe));

    List<Product> content = new ArrayList<>();
    content.add(productBook);
    content.add(productSubscribe);

    Page<Product> responsePage = new PageImpl<>(content, Pageable.ofSize(10), 2);
    //given
    given(productRepository.findById(productBook.getId())).willReturn(
        Optional.ofNullable(productBook));
    given(productRepository.findById(productSubscribe.getId())).willReturn(
        Optional.ofNullable(productSubscribe));
    given(productRepository.findNotDeletedByPageable(any())).willReturn(responsePage);

    //when
    Page<RetrieveProductResponse> result = productService.retrieveProductPage(
        PageRequest.of(0, 10));

    //then
    BDDMockito.then(productRepository).should().findNotDeletedByPageable(any());
    assertThat(result.getTotalElements()).isEqualTo(2L);
  }

  @Test
  void testRetrieveAdminProductPage_success() throws IOException {
    List<RetrieveProductResponse> finalContent = new ArrayList<>();
    finalContent.add(new RetrieveProductResponse(productBook, subscribe));

    List<Product> content = new ArrayList<>();
    content.add(productBook);
    content.add(productSubscribe);

    Page<Product> responsePage = new PageImpl<>(content, Pageable.ofSize(10), 2);
    //given
    given(productRepository.findById(productBook.getId())).willReturn(
        Optional.ofNullable(productBook));
    given(productRepository.findById(productSubscribe.getId())).willReturn(
        Optional.ofNullable(productSubscribe));
    given(productRepository.findAllBy(Pageable.ofSize(10), Product.class)).willReturn(responsePage);

    //when
    Page<RetrieveProductResponse> result = productService.retrieveAdminProductPage(
        PageRequest.of(0, 10));

    //then
    BDDMockito.then(productRepository).should().findAllBy(Pageable.ofSize(10), Product.class);
    assertThat(result.getTotalElements()).isEqualTo(2L);
  }

  @Test
  void testRetrieveBookDataForSubscribe_success() {
    Pageable pageable = Pageable.ofSize(20);
    List<RetrieveBookForSubscribeResponse> pageContent = new ArrayList<>();
    pageContent.add(
        new RetrieveBookForSubscribeResponse(productBook.getId(), productBook.getTitle(),
            productDetail.getPublisher()));

    Page<RetrieveBookForSubscribeResponse> thisPage = new PageImpl<>(pageContent, pageable, 1);

    given(productRepository.findAllBooksForSubscribeBy(pageable)).willReturn(thisPage);

    for (RetrieveBookForSubscribeResponse response : pageContent) {
      BookSubscribe.Pk pk = new BookSubscribe.Pk(subscribe.getId(), response.getProductId());
      given(bookSubscribeRepository.existsById(pk)).willReturn(true);

      List<String> authorNames = new ArrayList<>();
      authorNames.add("test Author");

      given(productRepository.findAuthorNameByProductId(response.getProductId())).willReturn(
          authorNames);

      response.setAuthors(authorNames);
      response.setIsRegistered(true);
    }

    Page<RetrieveBookForSubscribeResponse> result = productService.retrieveBookDataForSubscribe(
        pageable, subscribe.getId());

    assertThat(result.getTotalElements()).isEqualTo(1L);
  }

  @Test
  void testSoftDelete_success() {
    //given
    given(productRepository.findById(productBook.getId())).willReturn(
        Optional.ofNullable(productBook));
    assertThat(productBook.isDeleted()).isFalse();

    //when
    productService.softDelete(productBook.getId());

    //then
    assertThat(productBook.isDeleted()).isTrue();
  }

  @Test
  void testRetrieveBookProduct_success() throws IOException {

    //given
    Long productId = 1L;
    List<Long> productIds = new ArrayList<>();
    productIds.add(productId);
    List<RetrieveProductResponse> productResponses = new ArrayList<>();

    for (int i = 0; i < productIds.size(); i++) {
      given(productRepository.findById(productIds.get(i))).willReturn(
          Optional.ofNullable(productBook));

      given(productDetailRepository.existsProductDetailByProductId(productBook.getId())).willReturn(
          true);

      given(productDetailRepository.findProductDetailByProductId(productBook.getId())).willReturn(
          productDetail);

      List<RetrieveAuthorResponse> authors = new ArrayList<>();
      RetrieveAuthorResponse author = new RetrieveAuthorResponse(1L, "test");
      authors.add(author);
      // 작가 정보 DTO
      given(productDetailRepository.findAuthorsByProductDetailId(productDetail.getId())).willReturn(
          authors);

      // 합체
      RetrieveProductResponse element = new RetrieveProductResponse(productBook, productDetail,
          authors);
      // 컨텐츠에 주입
      productResponses.add(element);
    }

    //when
    List<RetrieveProductResponse> result = productService.retrieveProductResponses(productIds);

    assertThat(result.get(0).getPrice()).isEqualTo(productResponses.get(0).getPrice());
  }


  @Test
  void getAllProducts() {

    given(productRepository.findAllByIsDeletedOrderByCreatedAtDesc(false,
        PageRequest.of(0, 16))).willReturn(Page.empty());

    productService.getAllProducts(PageRequest.of(0, 16));

    then(productRepository).should(times(1))
        .findAllByIsDeletedOrderByCreatedAtDesc(false, PageRequest.of(0, 16));

  }

  @Test
  void getLatestEights() {

    given(productRepository.findTop8ByIsDeletedOrderByCreatedAtDesc(false))
        .willReturn(List.of());

    productService.getLatestEights();

    then(productRepository)
        .should(times(1)).findTop8ByIsDeletedOrderByCreatedAtDesc(false);

  }

  @Test
  void retrieveProductByRequest() {

    SearchIdRequest searchIdRequest = new SearchIdRequest();

    ReflectionTestUtils.setField(searchIdRequest, "classification", "categories");
    ReflectionTestUtils.setField(searchIdRequest, "id", 101001L);

    productService.retrieveProductByRequest(searchIdRequest, PageRequest.of(0, 16));

    then(productRepository).shouldHaveNoInteractions();

  }


  @Test
  @DisplayName("상품 Id 목록으로 상품 조회")
  void retrieveProductListByProductNoList() {
    productService.retrieveProductListByProductNoList(List.of());

    BDDMockito.then(productRepository).should().findAllById(List.of());
  }

  @Test
  @DisplayName("상품 아이디 리스트 받아서 페이지네이션")
  void getProductsPage() {
    Pageable pageable = PageRequest.of(0, 20);
    productService.getProductsPage(pageable);

    BDDMockito.then(productRepository).should().retrieveProductsInPage(pageable);

  }

  @Test
  @DisplayName("상품 번호로 상세 조회")
  void findProductById() {
    Long productId = 1L;
    productService.findProductById(productId);

    BDDMockito.then(productRepository).should().retrieveProductById(productId);
  }

  @Test
  @DisplayName("구매시 재고 처리 성공")
  void storageSoldOutChecker() throws NotEnoughStockException {
    List<CartDto> cartDtoList = new ArrayList<>();
    cartDtoList.add(new CartDto(1L, 1));
    for (CartDto cartDto : cartDtoList) {
      given(productDetailRepository.updateProductStock(cartDto.getProductNo(),
          (long) cartDto.getCount())).willReturn(1);
    }

    boolean result = productService.storageSoldOutChecker(cartDtoList);

    assertThat(result).isTrue();
  }

@Test
@DisplayName("결제 취소 시 재고 반환")
  void storageRefund() throws NotEnoughStockException {
  List<CartDto> cartDtoList = new ArrayList<>();
  cartDtoList.add(new CartDto(1L, 1));
    for (CartDto cartDto : cartDtoList) {
      given(productDetailRepository.addProductStock(cartDto.getProductNo(),
          (long) cartDto.getCount())).willReturn(1);
    }
  boolean result = productService.storageRefund(cartDtoList);

  assertThat(result).isTrue();
  }

}
