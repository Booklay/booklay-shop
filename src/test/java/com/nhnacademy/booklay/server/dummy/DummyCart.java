package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.PostType;
import com.nhnacademy.booklay.server.entity.ProductAskComment;
import org.springframework.test.util.ReflectionTestUtils;

public class DummyCart {
//  public static Cart getDummyCart(){
//    Member dummyMember = Dummy.getDummyMember();
//    Product dummyProduct = Dummy.getDummyProduct();
//    Cart.Pk dummyPk = new Pk(dummyMember.getMemberId(), dummyProduct.getId());
//
//    Cart cart = new Cart(dummyPk, dummyMember, dummyProduct);
//
//    return cart;
//  }


  public static PostType getDummyPostType() {
    PostType postType = PostType.builder()
        .postTypeId(1L)
        .type("1:1문의")
        .build();
    return postType;
  }

  public static Post getDummyPost() {
    PostType dummyPostType = getDummyPostType();

    Post post = Post.builder()
        .postTypeId(dummyPostType)
        .groupOrder(0L)
        .depth(0L)
        .title("dummy title")
        .content("dummy content for test")
        .isViewPublic(true)
        .build();

    ReflectionTestUtils.setField(post, "postId", 1L);
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

    ReflectionTestUtils.setField(comment, "commentId", 1L);

    return comment;
  }
}
