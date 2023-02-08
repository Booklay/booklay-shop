package com.nhnacademy.booklay.server.service.board;

import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.PostType;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.post.PostRepository;
import com.nhnacademy.booklay.server.repository.post.PostTypeRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final ProductRepository productRepository;
  private final PostTypeRepository postTypeRepository;

  private final MemberRepository memberRepository;

  @Override
  public void createPost(BoardPostCreateRequest request) {
    PostType postType = postTypeRepository.findById(request.getPostTypeNo())
        .orElseThrow(() -> new NotFoundException(PostType.class, "post type not found"));
    Member writer = memberRepository.findById(request.getMemberNo())
        .orElseThrow(() -> new NotFoundException(Member.class, "member not found"));
    Post post = Post.builder()
        .postTypeId(postType)
        .memberId(writer)
        .groupOrder(request.getGroupOrder())
        .depth(request.getDepth())
        .title(request.getTitle())
        .content(request.getContent())
        .isViewPublic(request.getViewPublic())
        .build();

    if (request.getProductNo() != null) {
      Product product = productRepository.findById(request.getProductNo())
          .orElseThrow(() -> new NotFoundException(Product.class, "product not found"));
      post.setProductId(product);
    }
    if (request.getGroupPostNo() != null) {
      Post groupPost = postRepository.findById(request.getGroupPostNo())
          .orElseThrow(() -> new NotFoundException(Post.class, "post not found"));
      post.setGroupNo(groupPost);
    }
    if (request.getAnswered() != null) {
      post.setAnswered(request.getAnswered());
    }

    postRepository.save(post);
  }
}
