package com.nhnacademy.booklay.server.service.product.impl;

import com.nhnacademy.booklay.server.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.server.dto.product.CreateProductBookRequest;
import com.nhnacademy.booklay.server.dto.product.CreateProductSubscribeRequest;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.entity.CategoryProduct.Pk;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.repository.CategoryRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import com.nhnacademy.booklay.server.service.product.CategoryProductService;
import com.nhnacademy.booklay.server.service.product.ProductAuthorService;
import com.nhnacademy.booklay.server.service.product.ProductDetailService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 최규태
 */

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;

  private final ProductDetailService productDetailService;
  private final CategoryRepository categoryRepository;
  private final CategoryProductService categoryProductService;
  private final AuthorService authorService;
  private final ProductAuthorService productAuthorService;
  private final SubscribeService subscribeService;

  @Override
  @Transactional
  public Long createBookProduct(CreateProductBookRequest request) throws Exception {
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

    ProductDetail savedDetail = productDetailService.createProductDetail(productDetail);

    //product_author
    productAuthorRegister(request.getAuthorIds(), savedDetail);

    return savedProduct.getId();
  }

  @Override
  @Transactional
  public Long createSubscribeProduct(CreateProductSubscribeRequest request) {
    Product product = splitProductSubscribe(request);
    Product savedProduct = productRepository.save(product);

    //category_product
    saveProductCategory(request.getCategoryIds(), savedProduct);

    //subscribe
    Subscribe subscribe = splitSubscribe(savedProduct, request);

    if (Objects.nonNull(request.getPublisher())) {
      subscribe.setPublisher(request.getPublisher());
    }

    subscribeService.createSubscribe(subscribe);

    return savedProduct.getId();
  }

  @Override
  @Transactional
  public Long updateBookProduct(CreateProductBookRequest request) throws Exception {
    if (!productRepository.existsById(request.getProductId())) {
      throw new IllegalArgumentException();
    }

    Product product = splitProduct(request);
    product.setId(request.getProductId());
    Product updateProduct = productRepository.save(product);

    categoryProductService.deleteAllByProductId(updateProduct.getId());
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

    ProductDetail updatedDetail = productDetailService.createProductDetail(productDetail);

    //싹 밀었다가 다시 다 등록하는걸로 생각하는데 그게 맞나?
    productAuthorService.deleteProductAuthors(updatedDetail.getId());
    productAuthorRegister(request.getAuthorIds(), updatedDetail);
    return null;
  }


  @Override
  @Transactional
  public Long updateSubscribeProduct(CreateProductSubscribeRequest request) throws Exception {
    if (!productRepository.existsById(request.getProductId())) {
      throw new IllegalArgumentException();
    }

    Product product = splitProductSubscribe(request);
    Product savedProduct = productRepository.save(product);

    //category_product
    categoryProductService.deleteAllByProductId(savedProduct.getId());
    saveProductCategory(request.getCategoryIds(), savedProduct);

    //subscribe
    Subscribe subscribe = splitSubscribe(savedProduct, request);
    subscribe.setId(request.getSubscribeId());

    if (Objects.nonNull(request.getPublisher())) {
      subscribe.setPublisher(request.getPublisher());
    }

    subscribeService.updateSubscribeById(subscribe);

    return savedProduct.getId();
  }

  //공통

  //카테고리 등록
  void saveProductCategory(List<Long> categories, Product product) {
    for (int i = 0; i < categories.size(); i++) {
      CategoryProduct.Pk pk = new Pk(product.getId(), categories.get(i));

      Category category = categoryRepository.findById(categories.get(i)).orElseThrow(()-> new IllegalArgumentException("catrgory not found"));

      CategoryProduct categoryProduct = CategoryProduct.builder()
          .pk(pk)
          .product(product)
          .category(category)
          .build();

      categoryProductService.createCategoryProduct(categoryProduct);
    }
  }

  //book a.k.a product detail

  //dto 에서 product 분리
  private Product splitProduct(CreateProductBookRequest request) {
    return Product.builder()
        .price(request.getPrice())
        .pointMethod(request.isPointMethod())
        .pointRate(request.getPointRate())
        .title(request.getTitle())
        .shortDescription(request.getShortDescription())
        .longDescription(request.getLongDescription())
        .image(request.getImage())
        .isSelling(request.isSelling())
        .build();
  }

  //product_author 등록
  private void productAuthorRegister(List<Long> authorIdList, ProductDetail productDetail)
      throws Exception {
    for (int i = 0; i < authorIdList.size(); i++) {
      Author foundAuthor = authorService.retrieveAuthorById(authorIdList.get(i));

      ProductAuthor.Pk pk = new ProductAuthor.Pk(productDetail.getId(), foundAuthor.getAuthorNo());

      ProductAuthor productAuthor = ProductAuthor.builder()
          .pk(pk)
          .author(foundAuthor)
          .productDetail(productDetail)
          .build();

      productAuthorService.createProductAuthor(productAuthor);
    }
  }

  //dto 에서 product_detail 분리
  private ProductDetail splitDetail(CreateProductBookRequest request, Product savedProduct) {
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
  private Product splitProductSubscribe(CreateProductSubscribeRequest request) {
    return Product.builder()
        .price(request.getPrice())
        .pointMethod(request.isPointMethod())
        .pointRate(request.getPointRate())
        .title(request.getTitle())
        .shortDescription(request.getShortDescription())
        .longDescription(request.getLongDescription())
        .image(request.getImage())
        .isSelling(request.isSelling())
        .build();
  }

  private Subscribe splitSubscribe(Product product, CreateProductSubscribeRequest request) {
    return Subscribe.builder()
        .product(product)
        .subscribeWeek(request.getSubscribeWeek())
        .subscribeDay(request.getSubscribeDay())
        .build();
  }
}
