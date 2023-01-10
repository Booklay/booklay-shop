package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.Cart;
import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.PostType;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.ProductAskComment;
import com.nhnacademy.booklay.server.entity.ProductAuthor;
import com.nhnacademy.booklay.server.entity.ProductDetail;
import java.time.LocalDate;

public class DummyCart {

  public static Cart getDummyCart() {
    Member dummyMember = Dummy.getDummyMember();
    Product dummyProduct = getDummyProduct();
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

  public static Product getDummyProduct() {
    Image dummyImage = getDummyImage();
    Product product = Product.builder()
        .price(1000L)
        .image(getDummyImage())
        .title("new product")
        .longDescription("veeeeeeeeery looooooooooooooong description")
        .shortDescription("short description")
        .pointMethod(true)
        .pointRate(5L)
        .isSelling(true)
        .build();

    return product;
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
        .authorNo(1L)
        .name("작가님")
        .build();

    return author;
  }

  public static ProductDetail getDummyProductDetail(){
    Product dummyProduct = getDummyProduct();
    ProductDetail productDetail = ProductDetail.builder()
        .product(dummyProduct)
        .isbn("9128-1231-123")
        .publisher("출판사")
        .page(200)
        .publishedDate(LocalDate.of(1997,07,25))
        .build();

    productDetail.setStorage(300);

    return productDetail;
  }
  public static ProductAuthor getDummyProductAuthor(){
    Author dummyAuthor = getDummyAuthor();
    ProductDetail dummyProductDetail = getDummyProductDetail();

    ProductAuthor.Pk pk = new ProductAuthor.Pk(dummyAuthor.getAuthorNo(), dummyProductDetail.getId());

    ProductAuthor productAuthor = ProductAuthor.builder()
        .pk(pk)
        .author(dummyAuthor)
        .productDetail(dummyProductDetail)
        .build();

    return productAuthor;
  }
}
