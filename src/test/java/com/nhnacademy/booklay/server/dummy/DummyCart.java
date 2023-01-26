package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.dto.product.request.CreateUpdateProductBookRequest;
import com.nhnacademy.booklay.server.entity.*;
import com.nhnacademy.booklay.server.entity.CategoryProduct.Pk;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

public class DummyCart {

  public static Cart getDummyCart() {
    CreateUpdateProductBookRequest request = getDummyProductBookDto();
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

  public static Product getDummyProduct(CreateUpdateProductBookRequest request) {
    return Product.builder()
        .image(getDummyImage())
        .price(request.getPrice())
        .pointMethod(request.isPointMethod())
        .pointRate(request.getPointRate())
        .title(request.getTitle())
        .shortDescription(request.getShortDescription())
        .longDescription(request.getLongDescription())
        .image(getDummyImage())
        .isSelling(request.isSelling())
        .build();
  }

  public static Image getDummyImage() {
    return Image.builder()
        .ext("jpg")
        .address("dummy_address")
        .build();
  }

  public static Author getDummyAuthor() {

    Author author = Author.builder()
        .name("작가님")
        .build();

    author.setAuthorId(1L);

    return author;
  }

  public static ProductDetail getDummyProductDetail(CreateUpdateProductBookRequest request) {
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

  public static ProductAuthor getDummyProductAuthor(CreateUpdateProductBookRequest request) {
    Author dummyAuthor = getDummyAuthor();
    ProductDetail dummyProductDetail = getDummyProductDetail(request);

    ProductAuthor.Pk pk = new ProductAuthor.Pk(dummyAuthor.getAuthorId(),
        dummyProductDetail.getId());

    ProductAuthor productAuthor = ProductAuthor.builder()
        .pk(pk)
        .author(dummyAuthor)
        .productDetail(dummyProductDetail)
        .build();

    return productAuthor;
  }

  public static CreateUpdateProductBookRequest getDummyProductBookDto() {
    MultipartFile file = null;

    List<Long> authors = new ArrayList<>();
    authors.add(1L);

    List<Long> categories = new ArrayList<>();
    categories.add(1L);

    CreateUpdateProductBookRequest createProductBookRequest = CreateUpdateProductBookRequest.builder()
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
    createProductBookRequest.setImage(file);

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

  public static CategoryProduct getDummyCategoryProduct(CreateUpdateProductBookRequest request) {
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
