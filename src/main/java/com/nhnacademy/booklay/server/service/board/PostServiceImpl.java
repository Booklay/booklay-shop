package com.nhnacademy.booklay.server.service.board;

import com.nhnacademy.booklay.server.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.server.dto.board.request.BoardPostUpdateRequest;
import com.nhnacademy.booklay.server.dto.board.response.PostResponse;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.entity.PostType;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.post.PostRepository;
import com.nhnacademy.booklay.server.repository.post.PostTypeRepository;
import com.nhnacademy.booklay.server.repository.product.ProductRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final ProductRepository productRepository;
  private final PostTypeRepository postTypeRepository;
  private final MemberRepository memberRepository;

  @Override
  public Long createPost(BoardPostCreateRequest request) {
    PostType postType = postTypeRepository.findById(request.getPostTypeNo())
        .orElseThrow(() -> new NotFoundException(PostType.class, "post type not found"));
    Member writer = memberRepository.findById(request.getMemberNo())
        .orElseThrow(() -> new NotFoundException(Member.class, "member not found"));

    Post post = Post.builder()
        .postTypeId(postType)
        .memberId(writer)
        .depth(request.getDepth())
        .title(request.getTitle())
        .content(request.getContent())
        .isViewPublic(request.getViewPublic())
        .isDeleted(false)
        .build();

    if (request.getProductNo() != null) {
      Product product = productRepository.findById(request.getProductNo())
          .orElseThrow(() -> new NotFoundException(Product.class, "product not found"));
      post.setProductId(product);
    }
    if (request.getAnswered() != null) {
      post.setAnswered(request.getAnswered());
    }

    //요청에 그룹 번호가 지정되어 있다면
    if (request.getGroupNo() != null) {
      Post groupPost = postRepository.findById(request.getGroupNo())
          .orElseThrow(() -> new NotFoundException(Post.class, "post not found"));
      post.setGroupNo(groupPost);

      //요청에 order 요구사항이 없다면 숫자 센 다음 +1
      if (Objects.isNull(request.getGroupOrderNo())) {
        Integer currentOrderNo = postRepository.countChildByGroupNo(groupPost.getRealGroupNo());
        post.setGroupOrder(currentOrderNo + 1);
      }

      //요청에 order 가 들어있다면
      if (Objects.nonNull(request.getGroupOrderNo())) {
        //그데로 넣어주고
        post.setGroupOrder(request.getGroupOrderNo());

        //상위 order 전부 +1해서 수정 저장
        postRepository.updateUpperPostByGroupNoPostId(request.getGroupNo(),
            request.getGroupOrderNo());
      }
    }
    //요청에 그룹번호가 없는 아예 첫 게시글이라면 그룹번호 지정 후 오더 0으로 설정
    if (request.getGroupNo() == null) {
      Integer base_groupOrder = 0;
      post.setGroupOrder(base_groupOrder);
      Post saveForGroupNo = postRepository.save(post);
      post.setGroupNo(saveForGroupNo);
    }

    Post savedPost = postRepository.save(post);

    return savedPost.getPostId();
  }

  @Override
  public Long updatePost(BoardPostUpdateRequest request) {
    Post post = postRepository.findById(request.getPostId()).orElse(null);

    post.setTitle(request.getTitle());
    post.setContent(request.getContent());
    post.setViewPublic(request.getViewPublic());

    postRepository.save(post);

    return post.getPostId();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> retrieveProductQNA(Long productId, Pageable pageable) {
    return postRepository.findAllByProductIdPage(productId, pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public PostResponse retrievePostById(Long postId) {
    Post post = postRepository.findById(postId).orElse(null);

    PostResponse response = new PostResponse(post);
    if (Objects.nonNull(post.getProductId())) {
      response.setAuthorList(productRepository.getAuthorsByProductId(post.getProductId().getId()));
    }
    return response;
  }

  @Override
  public void deletePost(Long memberId, Long postId) {
    postRepository.deleteByPostIdAndMemberNo(postId, memberId);
  }
}
