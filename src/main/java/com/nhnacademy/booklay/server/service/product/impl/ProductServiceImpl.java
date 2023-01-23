package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberForAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductSubscribeRequest;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.server.dto.product.response.RetrieveProductViewResponse;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.entity.CategoryProduct.Pk;
import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.ProductTag;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import com.nhnacademy.booklay.server.repository.ImageRepository;
import com.nhnacademy.booklay.server.repository.product.AuthorRepository;
import com.nhnacademy.booklay.server.repository.product.CategoryProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductAuthorRepository;
import com.nhnacademy.booklay.server.repository.product.ProductDetailRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.repository.product.ProductTagRepository;
import com.nhnacademy.booklay.server.repository.product.SubscribeRepository;
import com.nhnacademy.booklay.server.service.product.ProductService;
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
  private final ImageRepository imageRepository;
  private final ProductTagRepository productTagRepository;

  @Override
  @Transactional
  public Long createBookProduct(CreateUpdateProductBookRequest request) throws Exception {

    //product
    Product product = splitProduct(request);
    Product savedProduct = productRepository.save(product);

    //category_product
    saveProductCategory(request.getCategoryIds(), savedProduct);

    //product_detail
    ProductDetail productDetail = splitDetail(request, savedProduct);
    if (Objects.nonNull(request.getStorage())) {
      productDetail.setStorage(request.getStorage());
    }
    if (Objects.nonNull(request.getEbookAddress())) {
      productDetail.setEbookAddress(request.getEbookAddress());
    }

    ProductDetail savedDetail = productDetailRepository.save(productDetail);

    //product_author
    productAuthorRegister(request.getAuthorIds(), savedDetail);

    return savedProduct.getId();
  }

  @Override
  @Transactional
  public Long createSubscribeProduct(CreateUpdateProductSubscribeRequest request) {
    Product product = splitProductSubscribe(request);
    Product savedProduct = productRepository.save(product);

    //category_product
    saveProductCategory(request.getCategoryIds(), savedProduct);

    //subscribe
    Subscribe subscribe = splitSubscribe(savedProduct, request);

    if (Objects.nonNull(request.getPublisher())) {
      subscribe.setPublisher(request.getPublisher());
    }

    subscribeRepository.save(subscribe);

    return savedProduct.getId();
  }

  @Override
  @Transactional
  public Long updateBookProduct(CreateUpdateProductBookRequest request) throws Exception {
    //TODO : 예외처리 만들것
    if (!productRepository.existsById(request.getProductId())) {
      throw new IllegalArgumentException();
    }

    Product product = splitProduct(request);
    product.setId(request.getProductId());
    Product updateProduct = productRepository.save(product);

    categoryProductRepository.deleteAllByProductId(updateProduct.getId());
    saveProductCategory(request.getCategoryIds(), updateProduct);

    //product detail
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

    //싹 밀었다가 다시 다 등록하는걸로 생각하는데 그게 맞나?
    productAuthorRepository.deleteAllByProductDetailId(updatedDetail.getId());
    productAuthorRegister(request.getAuthorIds(), updatedDetail);
    return null;
  }

  //상품(책 구독 모두) 게시판식 조회
  @Override
  @Transactional
  public Page<RetrieveProductResponse> retrieveProductPage(Pageable pageable) {
    Page<Product> products = productRepository.findAllBy(pageable, Product.class);

    List<Product> productsContent = products.getContent();

    List<RetrieveProductResponse> assembledContent = new ArrayList<>();

    for (int i = 0; i < productsContent.size(); i++) {
      Product product = productsContent.get(i);
//      if (product.isSelling()) {
      //책 상품이라면
      if (productDetailRepository.existsProductDetailByProduct(product)) {
        ProductDetail productDetail = productDetailRepository.findProductDetailByProduct(product);
        List<ProductAuthor> productAuthors = productAuthorRepository.findAllByProductDetail(
            productDetail);

        //작가 정보 DTO
        List<RetrieveAuthorResponse> authors = retrieveAuthorList(productDetail);
        //합체
        RetrieveProductResponse element = new RetrieveProductResponse(product, productDetail,
            authors);
        //컨텐츠에 주입
        assembledContent.add(element);
      }

      //구독 상품 이라면
      if (subscribeRepository.existsSubscribeByProduct(product)) {
        Subscribe subscribe = subscribeRepository.findSubscribeByProduct(product);
        RetrieveProductResponse element = new RetrieveProductResponse(product, subscribe);
        assembledContent.add(element);
      }
//      }
    }

    return new PageImpl<>(assembledContent, products.getPageable(),
        products.getTotalElements());
  }

  @Override
  public RetrieveProductViewResponse retrieveProductView(Long productId) {
    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new NotFoundException(Product.class, "product not found"));

    List<ProductTag> productTags = productTagRepository.findAllByPk_ProductId(product.getId());

    if (productDetailRepository.existsProductDetailByProduct(product)) {
      ProductDetail productDetail = productDetailRepository.findProductDetailByProduct(product);

      //작가 정보 DTO
      List<RetrieveAuthorResponse> authors = retrieveAuthorList(productDetail);

      return new RetrieveProductViewResponse(product, productDetail, authors);
    }

    if (subscribeRepository.existsSubscribeByProduct(product)) {
      Subscribe subscribe = subscribeRepository.findSubscribeByProduct(product);

      return new RetrieveProductViewResponse(product, subscribe);
    }
    return null;
  }

  @Override
  @Transactional
  public Long updateSubscribeProduct(CreateUpdateProductSubscribeRequest request) {
    if (!productRepository.existsById(request.getProductId())) {
      throw new IllegalArgumentException();
    }

    Product product = splitProductSubscribe(request);
    Product savedProduct = productRepository.save(product);

    //category_product
    categoryProductRepository.deleteAllByProductId(savedProduct.getId());
    saveProductCategory(request.getCategoryIds(), savedProduct);

    //subscribe
    Subscribe subscribe = splitSubscribe(savedProduct, request);

    if (Objects.nonNull(request.getPublisher())) {
      subscribe.setPublisher(request.getPublisher());
    }

    if (!subscribeRepository.existsById(subscribe.getId())) {
      throw new IllegalArgumentException();
    }
    subscribeRepository.save(subscribe);

    return savedProduct.getId();
  }

  //공통

  //카테고리 등록
  void saveProductCategory(List<Long> categories, Product product) {
    for (int i = 0; i < categories.size(); i++) {
      CategoryProduct.Pk pk = new Pk(product.getId(), categories.get(i));

      Category category = categoryRepository.findById(categories.get(i))
          .orElseThrow(() -> new IllegalArgumentException("catrgory not found"));

      CategoryProduct categoryProduct = CategoryProduct.builder()
          .pk(pk)
          .product(product)
          .category(category)
          .build();

      categoryProductRepository.save(categoryProduct);
    }
  }

  //book a.k.a product detail

  //dto 에서 product 분리
  private Product splitProduct(CreateUpdateProductBookRequest request) {
    MultipartFile thumbnail = request.getImage();

    log.info("출력 : " + thumbnail.getOriginalFilename());

    Image imageMake = Image.builder()
        .address(thumbnail.getOriginalFilename())
        .ext("jpeg")
        .build();

    Image image = imageRepository.save(imageMake);

    return Product.builder()
        .price(request.getPrice())
        .pointMethod(request.isPointMethod())
        .pointRate(request.getPointRate())
        .title(request.getTitle())
        .shortDescription(request.getShortDescription())
        .longDescription(request.getLongDescription())
        .image(image)
        .isSelling(request.isSelling())
        .build();
  }

  //product_author 등록
  private void productAuthorRegister(List<Long> authorIdList, ProductDetail productDetail) {
    for (int i = 0; i < authorIdList.size(); i++) {
      Author foundAuthor = authorRepository.findById(authorIdList.get(i))
          .orElseThrow(() -> new IllegalArgumentException());

      ProductAuthor.Pk pk = new ProductAuthor.Pk(productDetail.getId(), foundAuthor.getAuthorNo());

      ProductAuthor productAuthor = ProductAuthor.builder()
          .pk(pk)
          .author(foundAuthor)
          .productDetail(productDetail)
          .build();

      productAuthorRepository.save(productAuthor);
    }
  }

  //dto 에서 product_detail 분리
  private ProductDetail splitDetail(CreateUpdateProductBookRequest request, Product savedProduct) {
    //product detail
    return ProductDetail.builder()
        .product(savedProduct)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher(request.getPublisher())
        .publishedDate(request.getPublishedDate())
        .build();
  }

  //subscribe

  //dto 에서 product 분리
  private Product splitProductSubscribe(CreateUpdateProductSubscribeRequest request) {
    Image requestImage = Image.builder()
        .address(request.getImage().getOriginalFilename())
        .ext("jpeg")
        .build();

    Image image = imageRepository.save(requestImage);
    return Product.builder()
        .price(request.getPrice())
        .pointMethod(request.isPointMethod())
        .pointRate(request.getPointRate())
        .title(request.getTitle())
        .shortDescription(request.getShortDescription())
        .longDescription(request.getLongDescription())
        .image(image)
        .isSelling(request.isSelling())
        .build();
  }

  //TODO: toIntExact 수정해야한다
  private Subscribe splitSubscribe(Product product, CreateUpdateProductSubscribeRequest request) {
    return Subscribe.builder()
        .product(product)
        .subscribeWeek(Math.toIntExact(request.getSubscribeWeek()))
        .subscribeDay(Math.toIntExact(request.getSubscribeDay()))
        .build();
  }

  //작가 목록 뽑기
  private List<RetrieveAuthorResponse> retrieveAuthorList(ProductDetail productDetail) {
    List<ProductAuthor> productAuthors = productAuthorRepository.findAllByProductDetail(
        productDetail);
    //작가 정보 DTO
    List<RetrieveAuthorResponse> authors = new ArrayList<>();

    for (int j = 0; j < productAuthors.size(); j++) {
      ProductAuthor productAuthor = productAuthors.get(j);
      Author author = productAuthor.getAuthor();

      RetrieveAuthorResponse authorResponse = new RetrieveAuthorResponse(author.getAuthorNo(),
          author.getName());
      if (Objects.nonNull(author.getMember())) {
        MemberForAuthorResponse memberResponse = new MemberForAuthorResponse(
            author.getMember().getMemberNo(), author.getMember().getMemberId());
        authorResponse.setMember(memberResponse);
      }

      authors.add(authorResponse);
    }

    return authors;
  }
}
