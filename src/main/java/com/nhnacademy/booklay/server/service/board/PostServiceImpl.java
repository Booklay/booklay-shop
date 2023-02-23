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
import java.util.List;
import java.util.Objects;

import com.nhnacademy.booklay.server.service.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.nhnacademy.booklay.server.utils.CacheKeyName.POST_RESPONSE_PAGE_CACHE;
import static com.nhnacademy.booklay.server.utils.CacheKeyName.POST_RESPONSE_PAGE_CACHE;

/**
 * @Author:최규태
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {

  private final RedisCacheService redisCacheService;
  private final PostRepository postRepository;
  private final ProductRepository productRepository;
  private final PostTypeRepository postTypeRepository;
  private final MemberRepository memberRepository;

  private static final Integer POST_TYPE_NOTICE = 5;
  private static final String POST_NOT_FOUND = "post not found";

  /**
   * 게시글 생성
   *
   * @param request
   * @return
   */
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
          .orElseThrow(() -> new NotFoundException(Post.class, POST_NOT_FOUND));
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
      Integer baseGroupOrder = 0;
      post.setGroupOrder(baseGroupOrder);
      Post saveForGroupNo = postRepository.save(post);
      post.setGroupNo(saveForGroupNo);
    }

    Post savedPost = postRepository.save(post);

    redisCacheService.deleteCache(POST_RESPONSE_PAGE_CACHE, request.getProductNo());
    return savedPost.getPostId();
  }

  /**
   * 게시글 수정
   *
   * @param request
   * @return
   */
  @Override
  public Long updatePost(BoardPostUpdateRequest request) {
    Post post = postRepository.findById(request.getPostId()).orElseThrow(() -> new NotFoundException(Post.class, POST_NOT_FOUND));

    post.setTitle(request.getTitle());
    post.setContent(request.getContent());
    post.setViewPublic(request.getViewPublic());

    postRepository.save(post);
    redisCacheService.deleteCache(POST_RESPONSE_PAGE_CACHE, post.getProductNo());
    return post.getPostId();
  }

  /**
   * 답변 승인
   *
   * @param postId
   * @return
   */
  @Override
  public Long updateConfirmAnswer(Long postId) {
    postRepository.confirmAnswerByPostId(postId);
    return postId;
  }

  /**
   * 상품 QNA 게시판 조회
   *
   * @param productId
   * @param pageable
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> retrieveProductQNA(Long productId, Pageable pageable) {
    return postRepository.findAllByProductIdPage(productId, pageable);
  }

  /**
   * 공지사항 게시판 조회
   *
   * @param pageable
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PostResponse> retrieveNotice(Pageable pageable) {
    return postRepository.findAllNotice(POST_TYPE_NOTICE, pageable);
  }

  /**
   * pageLimit 수 만큼 최신 공지사항 조회
   *
   * @param pageLimit
   * @return
   */
  @Override
  public List<PostResponse> retrieveNoticeList(Integer pageLimit) {
    return postRepository.findNoticeList(pageLimit);
  }

  /**
   * 게시글 조회
   *
   * @param postId
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public PostResponse retrievePostById(Long postId) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException(Post.class, POST_NOT_FOUND));

    PostResponse response = new PostResponse(post);
    if (Objects.nonNull(post.getProductId())) {
      response.setAuthorList(productRepository.getAuthorsByProductId(post.getProductId().getId()));
    }
    return response;
  }

  /**
   * 게시글 소프트 딜리트
   *
   * @param memberId
   * @param postId
   */
  @Override
  public void deletePost(Long memberId, Long postId) {
    Post post = postRepository.findPostByPostIdAndMemberId(postId, memberId);
    if (post == null){
      return;
    }
    postRepository.deleteByPostIdAndMemberNo(postId, memberId);
    redisCacheService.deleteCache(POST_RESPONSE_PAGE_CACHE, post.getProductNo());
  }
}
