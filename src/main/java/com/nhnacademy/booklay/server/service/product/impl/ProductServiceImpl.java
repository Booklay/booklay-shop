package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductSubscribeResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductViewResponse;
import com.nhnacademy.booklay.server.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.BookSubscribe;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.entity.CategoryProduct.Pk;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.category.CategoryRepository;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.repository.product.BookSubscribeRepository;
import com.nhnacademy.booklay.server.repository.product.CategoryProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductTagRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.storage.FileService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 최규태
 */

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final CategoryRepository categoryRepository;
  private final ProductDetailRepository productDetailRepository;
  private final CategoryProductRepository categoryProductRepository;
  private final AuthorRepository authorRepository;
  private final ProductAuthorRepository productAuthorRepository;
  private final SubscribeRepository subscribeRepository;
  private final FileService fileService;
  private final BookSubscribeRepository bookSubscribeRepository;
  private static final Long NOT_FOUND_PRODUCT_ID = 33L;

  /**
   * 서적 상품 생성
   *
   * @param request
   * @return
   * @throws Exception
   */
  @Override
  public Long createBookProduct(CreateUpdateProductBookRequest request) throws Exception {

    // product
    Product product = splitProduct(request);
    Product savedProduct = productRepository.save(product);

    // category_product
    saveProductCategory(request.getCategoryIds(), savedProduct);

    // product_detail
    ProductDetail productDetail = splitDetail(request, savedProduct);
    if (Objects.nonNull(request.getStorage())) {
      productDetail.setStorage(request.getStorage());
    }
    if (Objects.nonNull(request.getEbookAddress())) {
      productDetail.setEbookAddress(request.getEbookAddress());
    }
    ProductDetail savedDetail = productDetailRepository.save(productDetail);

    // product_author
    productAuthorRegister(request.getAuthorIds(), savedDetail);

    return savedProduct.getId();
  }

  /**
   * 구독 상품 생성
   *
   * @param request
   * @return
   * @throws IOException
   */
  @Override
  public Long createSubscribeProduct(CreateUpdateProductSubscribeRequest request)
      throws IOException {
    Product product = splitProductSubscribe(request);
    Product savedProduct = productRepository.save(product);
    // category_product
    saveProductCategory(request.getCategoryIds(), savedProduct);
    // subscribe
    Subscribe subscribe = splitSubscribe(savedProduct, request);

    if (request.getPublisher() != null) {
      subscribe.setPublisher(request.getPublisher());
    }
    subscribeRepository.save(subscribe);
    return savedProduct.getId();
  }

  /**
   * 수정 위해서 책 상품 조회
   *
   * @param id
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public ProductAllInOneResponse retrieveBookData(Long id) {
    //TODO: 못찾는거 예외처리
    return productRepository.findProductById(id);
  }

  /**
   * 책 상품 수정 처리
   *
   * @param request
   * @return
   * @throws Exception
   */
  @Override
  public Long updateBookProduct(CreateUpdateProductBookRequest request) throws Exception {
    if (!productRepository.existsById(request.getProductId())) {
      throw new NotFoundException(Product.class, "product not found");
    }
    Product product = splitProduct(request);
    product.setId(request.getProductId());
    product.setCreatedAt(request.getCreatedAt());
    Product updateProduct = productRepository.save(product);

    categoryProductRepository.deleteAllByProductId(updateProduct.getId());
    saveProductCategory(request.getCategoryIds(), updateProduct);

    // product detail
    ProductDetail productDetail = splitDetail(request, updateProduct);
    productDetail.setId(request.getProductDetailId());

    if (Objects.nonNull(request.getStorage())) {
      productDetail.setStorage(request.getStorage());
    }
    if (Objects.nonNull(request.getEbookAddress())) {
      productDetail.setEbookAddress(request.getEbookAddress());
    }

    if (!productDetailRepository.existsById(productDetail.getId())) {
      throw new IllegalArgumentException();
    }
    ProductDetail updatedDetail = productDetailRepository.save(productDetail);

    productAuthorRepository.deleteAllByProductDetailId(updatedDetail.getId());
    productAuthorRegister(request.getAuthorIds(), updatedDetail);
    return null;
  }

  /**
   * 상품 soft delete
   *
   * @param productId
   */
  @Override
  public void softDelete(Long productId) {
    Product targetProduct = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException(Product.class, "product not found"));

    targetProduct.setDeleted(true);

    productRepository.save(targetProduct);
  }

  /**
   * 구독 상품 수정 처리
   *
   * @param request
   * @return
   * @throws IOException
   */
  @Override
  @Transactional
  public Long updateSubscribeProduct(CreateUpdateProductSubscribeRequest request)
      throws IOException {
    if (!productRepository.existsById(request.getProductId())) {
      throw new IllegalArgumentException();
    }

    Product product = splitProductSubscribe(request);
    product.setId(request.getProductId());
    product.setCreatedAt(request.getCreatedAt());
    Product savedProduct = productRepository.save(product);

    // category_product
    categoryProductRepository.deleteAllByProductId(savedProduct.getId());
    saveProductCategory(request.getCategoryIds(), savedProduct);

    // subscribe
    Subscribe subscribe = splitSubscribe(savedProduct, request);
    subscribe.setId(request.getSubscribeId());

    if (Objects.nonNull(request.getPublisher())) {
      subscribe.setPublisher(request.getPublisher());
    }

    if (!subscribeRepository.existsById(subscribe.getId())) {
      throw new IllegalArgumentException();
    }
    subscribeRepository.save(subscribe);

    return savedProduct.getId();
  }

  // 공통

  /**
   * 상풍에 카테고리 등록
   *
   * @param categories
   * @param product
   */
  void saveProductCategory(List<Long> categories, Product product) {
    for (int i = 0; i < categories.size(); i++) {
      CategoryProduct.Pk pk = new Pk(product.getId(), categories.get(i));

      Category category = categoryRepository.findById(categories.get(i))
          .orElseThrow(() -> new IllegalArgumentException(
              "category not found"));

      CategoryProduct categoryProduct = CategoryProduct.builder()
          .pk(pk)
          .product(product)
          .category(category)
          .build();

      categoryProductRepository.save(categoryProduct);
    }
  }

  // 책

  /**
   * 서적 생성 수정 dto에서 product 분리
   *
   * @param request
   * @return
   * @throws IOException
   */
  private Product splitProduct(CreateUpdateProductBookRequest request) throws IOException {
    MultipartFile thumbnail = request.getImage();

    ObjectFile objectFile = fileService.uploadFile(thumbnail);

    return Product.builder()
        .price(request.getPrice())
        .pointMethod(request.getPointMethod())
        .pointRate(request.getPointRate())
        .title(request.getTitle())
        .shortDescription(request.getShortDescription())
        .longDescription(request.getLongDescription())
        .objectFile(objectFile)
        .isSelling(request.getSelling())
        .build();
  }

  /**
   * 서적과 작가 조인 테이블에 등록
   *
   * @param authorIdList
   * @param productDetail
   */
  private void productAuthorRegister(List<Long> authorIdList, ProductDetail productDetail) {
    for (int i = 0; i < authorIdList.size(); i++) {
      Author foundAuthor = authorRepository.findById(authorIdList.get(i))
          .orElseThrow(IllegalArgumentException::new);

      ProductAuthor.Pk pk =
          new ProductAuthor.Pk(productDetail.getId(), foundAuthor.getAuthorId());

      ProductAuthor productAuthor = ProductAuthor.builder()
          .pk(pk)
          .author(foundAuthor)
          .productDetail(productDetail)
          .build();

      productAuthorRepository.save(productAuthor);
    }
  }

  /**
   * 서적 생성 수정 dto에서 product detail 분리
   *
   * @param request
   * @param savedProduct
   * @return
   */
  private ProductDetail splitDetail(CreateUpdateProductBookRequest request,
      Product savedProduct) {
    // product detail
    return ProductDetail.builder()
        .product(savedProduct)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher(request.getPublisher())
        .publishedDate(request.getPublishedDate())
        .build();
  }

  // 구독

  /**
   * 구독 상품 생성 수정 dto에서 product 분리
   *
   * @param request
   * @return
   * @throws IOException
   */
  private Product splitProductSubscribe(CreateUpdateProductSubscribeRequest request)
      throws IOException {
    MultipartFile thumbnail = request.getImage();

    ObjectFile objectFile = fileService.uploadFile(thumbnail);

    return Product.builder()
        .price(request.getPrice())
        .pointMethod(request.getPointMethod())
        .pointRate(request.getPointRate())
        .title(request.getTitle())
        .shortDescription(request.getShortDescription())
        .longDescription(request.getLongDescription())
        .objectFile(objectFile)
        .isSelling(request.getSelling())
        .build();
  }

  /**
   * 구독 상품 생성 수정 dto에서 subscribe 분리
   *
   * @param product
   * @param request
   * @return
   */
  private Subscribe splitSubscribe(Product product, CreateUpdateProductSubscribeRequest request) {
    return Subscribe.builder()
        .product(product)
        .build();
  }

  /**
   * 상품(서적 + 구독) 게시판식 조회
   *
   * @param pageable
   * @return
   * @throws IOException
   */
  @Override
  @Transactional(readOnly = true)
  public Page<RetrieveProductResponse> retrieveProductPage(Pageable pageable) throws IOException {
    Page<Product> products = productRepository.findNotDeletedByPageable(pageable);

    List<Long> productIds = refineProductsToLongList(products.getContent());
    List<RetrieveProductResponse> assembledContent = retrieveProductResponses(productIds);

    return new PageImpl<>(assembledContent, products.getPageable(),
        products.getTotalElements());
  }

  /**
   * 관리자의 상품(책 + 구독) 게시판식 조회 (soft delete 무시)
   *
   * @param pageable
   * @return
   * @throws IOException
   */
  @Override
  @Transactional(readOnly = true)
  public Page<RetrieveProductResponse> retrieveAdminProductPage(Pageable pageable)
      throws IOException {
    Page<Product> products = productRepository.findAllBy(pageable, Product.class);

    List<Long> productIds = refineProductsToLongList(products.getContent());

    List<RetrieveProductResponse> assembledContent = retrieveProductResponses(productIds);

    return new PageImpl<>(assembledContent, products.getPageable(),
        products.getTotalElements());
  }

  /**
   * 상품 목록에서 id 목록 뽑아내는 처리
   *
   * @param products
   * @return
   */
  private List<Long> refineProductsToLongList(List<Product> products) {
    List<Long> productIds = new ArrayList<>();

    for (Product product : products) {
      productIds.add(product.getId());
    }

    return productIds;
  }

  /**
   * 구독 하위 상품을 위한 목록 조회
   *
   * @param pageable
   * @param subscribeId
   * @return
   */
  @Override
  public Page<RetrieveBookForSubscribeResponse> retrieveBookDataForSubscribe(Pageable pageable,
      Long subscribeId) {
    Page<RetrieveBookForSubscribeResponse> thisPage =
        productRepository.findAllBooksForSubscribeBy(
            pageable);
    List<RetrieveBookForSubscribeResponse> pageContent = thisPage.getContent();

    for (RetrieveBookForSubscribeResponse response : pageContent) {
      BookSubscribe.Pk pk = new BookSubscribe.Pk(subscribeId, response.getProductId());
      Boolean isRegistered = bookSubscribeRepository.existsById(pk);

      List<String> authorNames = productRepository.findAuthorNameByProductId(
          response.getProductId());

      response.setAuthors(authorNames);
      response.setIsRegistered(isRegistered);
    }

    return new PageImpl<>(pageContent, thisPage.getPageable(),
        thisPage.getTotalElements());
  }

  /**
   * RetrieveProductResponse dto 조립 메소드
   *
   * @param productIds
   * @return
   * @throws IOException
   */
  @Override
  @Transactional(readOnly = true)
  public List<RetrieveProductResponse> retrieveProductResponses(List<Long> productIds)
      throws IOException {
    List<RetrieveProductResponse> resultList = new ArrayList<>();

    for (int i = 0; i < productIds.size(); i++) {
      Product product = productRepository.findById(productIds.get(i))
          .orElseThrow(() -> new NotFoundException(Product.class, "product not found"));

      // 책 상품이라면
      if (productDetailRepository.existsProductDetailByProductId(product.getId())) {
        ProductDetail productDetail =
            productDetailRepository.findProductDetailByProductId(
                product.getId());

        // 작가 정보 DTO
        List<RetrieveAuthorResponse> authors =
            productDetailRepository.findAuthorsByProductDetailId(
                productDetail.getId());

        // 합체
        RetrieveProductResponse element =
            new RetrieveProductResponse(product, productDetail,
                authors);
        // 컨텐츠에 주입
        resultList.add(element);
      }

      // 구독 상품 이라면
      if (subscribeRepository.existsSubscribeByProduct(product)) {
        Subscribe subscribe = subscribeRepository.findSubscribeByProduct(product);
        RetrieveProductResponse element =
            new RetrieveProductResponse(product, subscribe);
        resultList.add(element);
      }
    }

    return resultList;
  }

  /**
   * 상품 Id 목록으로 상품 조회
   *
   * @param productNoList
   * @return
   */
  @Override
  public List<Product> retrieveProductListByProductNoList(List<Long> productNoList) {
    return productRepository.findAllById(productNoList);
  }

  /**
   * 상품 아이디 리스트를 받아서 페이지네이션.
   */
  @Override
  @Transactional(readOnly = true)
  public Page<ProductAllInOneResponse> getProductsPage(Pageable pageable) {
    return productRepository.findProductPage(pageable);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<ProductAllInOneResponse> retrieveProductListByProductNoList(List<Long> productNoList,
      Pageable pageable) {
    return productRepository.retrieveProductPage(productNoList, pageable);
  }

  @Transactional(readOnly = true)
  @Override
  public ProductAllInOneResponse retrieveProductResponse(Long productId) {
    return productRepository.retrieveProductResponse(productId);
  }

  /**
   * 재고 처리용
   *
   * @param productDetail
   */
  public void storageSoldOutChecker(ProductDetail productDetail) {

    productDetail.setStorage(productDetail.getStorage() - 1);

    if (productDetail.getStorage() <= 0) {
      Product product = productDetail.getProduct();
      product.setSelling(false);
      productRepository.save(product);
    }
    productDetailRepository.save(productDetail);

  }
}

