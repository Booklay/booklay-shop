package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.dto.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.server.entity.*;
import com.nhnacademy.booklay.server.entity.CategoryProduct.Pk;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DummyCart {

  public static Cart getDummyCart() {
    CreateProductBookRequest request = getDummyProductBookDto();
    Member dummyMember = Dummy.getDummyMember();
    Product dummyProduct = getDummyProduct(request);
    Cart.Pk dummyPk = new Cart.Pk(dummyMember.getMemberNo(), dummyProduct.getId());

    Cart cart = Cart.builder()
        .pk(dummyPk)
        .member(dummyMember)
        .product(dummyProduct)
        .build();

    return cart;
  }


  public static PostType getDummyPostType() {
    PostType postType = PostType.builder()
        .postTypeId(1L)
        .type("1:1문의")
        .build();
    return postType;
  }

  public static Post getDummyPost() {
    PostType dummyPostType = getDummyPostType();
    Member dummyMember = Dummy.getDummyMember();

    Post post = Post.builder()
        .memberId(dummyMember)
        .postTypeId(dummyPostType)
        .groupOrder(0L)
        .depth(0L)
        .title("dummy title")
        .content("dummy content for test")
        .isViewPublic(true)
        .build();

    return post;
  }

  public static ProductAskComment getDummyComment() {
    Post dummyPost = getDummyPost();
    Member dummyMember = Dummy.getDummyMember();
    ProductAskComment comment = ProductAskComment.builder().postId(dummyPost)
        .content("dummy test comment")
        .depth(0L)
        .memberId(dummyMember)
        .groupOrder(0L)
        .build();

    return comment;
  }

  public static Product getDummyProduct(CreateProductBookRequest request) {
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

  public static Image getDummyImage() {
    Image image = Image.builder()
        .id(1L)
        .ext("dummy")
        .address("dummy address")
        .build();
    return image;
  }

  public static Author getDummyAuthor() {

    Author author = Author.builder()
        .name("작가님")
        .build();

    author.setAuthorNo(1L);

    return author;
  }

  public static ProductDetail getDummyProductDetail(CreateProductBookRequest request) {
    Product dummyProduct = getDummyProduct(request);
    ProductDetail productDetail = ProductDetail.builder()
        .product(dummyProduct)
        .page(request.getPage())
        .isbn(request.getIsbn())
        .publisher(request.getPublisher())
        .publishedDate(request.getPublishedDate())
        .build();

    if (Objects.nonNull(request.getStorage())) {
      productDetail.setStorage(request.getStorage());
    }
    if (Objects.nonNull(request.getEbookAddress())) {
      productDetail.setEbookAddress(request.getEbookAddress());
    }

    return productDetail;
  }

  public static ProductAuthor getDummyProductAuthor(CreateProductBookRequest request) {
    Author dummyAuthor = getDummyAuthor();
    ProductDetail dummyProductDetail = getDummyProductDetail(request);

    ProductAuthor.Pk pk = new ProductAuthor.Pk(dummyAuthor.getAuthorNo(),
        dummyProductDetail.getId());

    ProductAuthor productAuthor = ProductAuthor.builder()
        .pk(pk)
        .author(dummyAuthor)
        .productDetail(dummyProductDetail)
        .build();

    return productAuthor;
  }

  public static CreateProductBookRequest getDummyProductBookDto() {
    Image image = Image.builder()
        .id(1L)
        .address("c://downloads/dummy_image")
        .ext("jpg")
        .build();

    List<Long> authors = new ArrayList<>();
    authors.add(1L);

    List<Long> categories = new ArrayList<>();
    categories.add(1L);

    CreateProductBookRequest createProductBookRequest = CreateProductBookRequest.builder()
        .image(image)
        .isbn("923-2239-42-1")
        .page(300)
        .isSelling(true)
        .price(12900L)
        .authorIds(authors)
        .categoryIds(categories)
        .longDescription("really looooooooooooong description")
        .shortDescription("short")
        .pointMethod(true)
        .pointRate(5L)
        .title("dummy title")
        .publishedDate(LocalDate.of(2023, 1, 1))
        .publisher("더미 출판사")
        .build();

    createProductBookRequest.setStorage(400);

    return createProductBookRequest;
  }

  public static Category getDummyCategory() {

    Category allProduct = Category.builder()
        .id(1L)
        .parent(null)
        .name("전체 상품")
        .depth(0L)
        .isExposure(true)
        .build();

    return Category.builder()
        .id(101L)
        .parent(allProduct)
        .name("국내도서")
        .depth(allProduct.getDepth() + 1)
        .isExposure(true)
        .build();
  }

  public static CategoryProduct getDummyCategoryProduct(CreateProductBookRequest request) {
    Product product = getDummyProduct(request);
    Category category = getDummyCategory();

    CategoryProduct.Pk pk = new Pk(product.getId(), category.getId());

    return CategoryProduct.builder()
        .pk(pk)
        .product(product)
        .category(category)
        .build();
  }

}
