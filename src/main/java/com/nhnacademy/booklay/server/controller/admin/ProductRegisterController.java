package com.nhnacademy.booklay.server.controller.admin;

import com.nhnacademy.booklay.server.dto.category.CategoryDto;
import com.nhnacademy.booklay.server.dto.product.ProductBookDto;
import com.nhnacademy.booklay.server.dto.product.ProductSubscribeDto;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Category;
import com.nhnacademy.booklay.server.entity.CategoryProduct;
import com.nhnacademy.booklay.server.entity.CategoryProduct.Pk;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import com.nhnacademy.booklay.server.entity.Subscribe;
import com.nhnacademy.booklay.server.service.category.CategoryService;
import com.nhnacademy.booklay.server.service.product.AuthorService;
import com.nhnacademy.booklay.server.service.product.CategoryProductService;
import com.nhnacademy.booklay.server.service.product.ProductAuthorService;
import com.nhnacademy.booklay.server.service.product.ProductDetailService;
import com.nhnacademy.booklay.server.service.product.ProductService;
import com.nhnacademy.booklay.server.service.product.SubscribeService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 최규태
 */


@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class ProductRegisterController {

  private final ProductDetailService productDetailService;
  private final ProductService productService;
  private final AuthorService authorService;
  private final ProductAuthorService productAuthorService;
  private final CategoryService categoryService;
  private final CategoryProductService categoryProductService;
  private final SubscribeService subscribeService;

  //책 등록
  @PostMapping("/register/book")
  public Long postBookRegister(ProductBookDto request) throws Exception {

    //product
    Product product = splitProduct(request);
    Product savedProduct = productService.createProduct(product);

    //category-product
    saveProductCategory(request.getCategoryIds(), savedProduct);

    //product detail
    ProductDetail productDetail = splitDetail(request, savedProduct);

    if (Objects.nonNull(request.getStorage())) {
      productDetail.setStorage(request.getStorage());
    }
    if (Objects.nonNull(request.getEbookAddress())) {
      productDetail.setEbookAddress(request.getEbookAddress());
    }

    ProductDetail savedDetail = productDetailService.createProductDetail(productDetail);

    //TODO: 나중에 프론트 하면서 같이 다시 생각해볼것 -> 어떻게 등록 폼에서 검색해서 리스트뽑아서 전달할 것인가?
    //product_author
    productAuthorRegister(request.getAuthorIds(), savedDetail);

    //id 넘겨줘서 이걸로 프론트단에서 상세페이지 이동하게? 해야할듯
    return savedProduct.getId();
  }


  //책 수정
  @PostMapping("/update/book")
  public Long postBookUpdater(ProductBookDto request) throws Exception {

    Product product = splitProduct(request);
    Product updateProduct = productService.updateProduct(request.getProductId(), product);

    categoryProductService.deleteAllByProductId(updateProduct.getId());
    saveProductCategory(request.getCategoryIds(), updateProduct);

    //product detail
    ProductDetail productDetail = splitDetail(request, updateProduct);

    //TODO : null 이 아니던 것을 null 로 바꿔주면 어떻게 할 것인가? 고민해볼것. -> 싹 밀었다 재등록? -> 팀원 상의해볼것
    productDetail.setStorage(null);
    productDetail.setEbookAddress(null);

    if (Objects.nonNull(request.getStorage())) {
      productDetail.setStorage(request.getStorage());
    }
    if (Objects.nonNull(request.getEbookAddress())) {
      productDetail.setEbookAddress(request.getEbookAddress());
    }

    ProductDetail updatedDetail = productDetailService.updateProductDetail(
        request.getProductDetailId(), productDetail);

    //싹 밀었다가 다시 다 등록하는걸로 생각하는데 그게 맞나?
    productAuthorService.deleteProductAuthors(updatedDetail.getId());
    productAuthorRegister(request.getAuthorIds(), updatedDetail);

    //id 넘겨줘서 이걸로 프론트단에서 상세페이지 이동하게? 해야할듯
    return updateProduct.getId();
  }


  //구독
  @PostMapping("/register/subscribe")
  public Long postSubscribeRegister(ProductSubscribeDto request) {
    //product
    Product product = splitProductSubscribe(request);
    Product savedProduct = productService.createProduct(product);

    //category-product
    saveProductCategory(request.getCategoryIds(), savedProduct);

    Subscribe subscribe = splitSubscribe(savedProduct, request);

    if (Objects.nonNull(request.getPublisher())) {
      subscribe.setPublisher(request.getPublisher());
    }

    subscribeService.createSubscribe(subscribe);

    return savedProduct.getId();
  }

  //구독 수정
  @PostMapping("/update/subscribe")
  public Long postSubscribeUpdate(ProductSubscribeDto request) {
    //product
    Product product = splitProductSubscribe(request);
    Product updateProduct = productService.updateProduct(request.getProductId(), product);

    //category product
    categoryProductService.deleteAllByProductId(updateProduct.getId());
    saveProductCategory(request.getCategoryIds(), updateProduct);

    //subscribe
    Subscribe subscribe = splitSubscribe(updateProduct, request);
    subscribe.setPublisher(null);
    if (Objects.nonNull(request.getPublisher())) {
      subscribe.setPublisher(request.getPublisher());
    }

    subscribeService.updateSubscribeById(request.getSubscribeId(), subscribe);

    return request.getProductId();
  }

//Product Book Dto

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

  //dto 에서 product 분리
  private Product splitProduct(ProductBookDto request) {
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

  //dto 에서 product_detail 분리
  private ProductDetail splitDetail(ProductBookDto request, Product savedProduct) {
    //product detail
    return ProductDetail.builder()
        .product(savedProduct)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher(request.getPublisher())
        .publishedDate(request.getPublishedDate())
        .build();
  }

//Product Subscribe Dto

  //dto 에서 product 분리
  private Product splitProductSubscribe(ProductSubscribeDto request) {
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

  private Subscribe splitSubscribe(Product product, ProductSubscribeDto request) {
    return Subscribe.builder()
        .product(product)
        .subscribeWeek(request.getSubscribeWeek())
        .subscribeDay(request.getSubscribeDay())
        .build();
  }


  //공동
  void saveProductCategory(List<Long> categories, Product product) {
    for (int i = 0; i < categories.size(); i++) {
      CategoryProduct.Pk pk = new Pk(product.getId(), categories.get(i));

      CategoryDto categoryDto = categoryService.retrieveCategory(categories.get(i));
      Category category = Category.builder()
          .id(categoryDto.getId())
          .name(categoryDto.getName())
          .build();

      CategoryProduct categoryProduct = CategoryProduct.builder()
          .pk(pk)
          .product(product)
          .category(category)
          .build();

      categoryProductService.createCategoryProduct(categoryProduct);
    }
  }
}
