package com.nhnacademy.booklay.server.service.board;

import com.nhnacademy.booklay.server.dto.board.request.CommentRequest;
import com.nhnacademy.booklay.server.dto.board.response.CommentResponse;
import com.nhnacademy.booklay.server.entity.Comment;
import com.nhnacademy.booklay.server.entity.Member;
import com.nhnacademy.booklay.server.entity.Post;
import com.nhnacademy.booklay.server.exception.service.NotFoundException;
import com.nhnacademy.booklay.server.repository.member.MemberRepository;
import com.nhnacademy.booklay.server.repository.post.CommentRepository;
import com.nhnacademy.booklay.server.repository.post.PostRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

  private final CommentRepository commentRepository;
  private final MemberRepository memberRepository;
  private final PostRepository postRepository;

  @Override
  public Page<CommentResponse> retrieveCommentPage(Long postId, Pageable pageable) {
    return commentRepository.findAllByPostIdAndPageable(postId, pageable);
  }

  @Override
  public Long createComment(CommentRequest request) {
    log.info("출력 : " + request.getPostId());
    Post post = postRepository.findById(request.getPostId())
        .orElseThrow(() -> new NotFoundException(Post.class, "post not found"));
    Member writer = memberRepository.findById(request.getMemberNo())
        .orElseThrow(() -> new NotFoundException(Member.class, "member not found"));
    Comment comment = Comment.builder()
        .postId(post)
        .memberId(writer)
        .content(request.getContent())
        .depth(request.getDepth())
        .isDeleted(false)
        .build();

    //요청에 그룹 번호가 지정되어 있다면
    if (request.getGroupCommentNo() != null) {
      Comment groupComment = commentRepository.findById(request.getGroupCommentNo())
          .orElseThrow(() -> new NotFoundException(Post.class, "post not found"));
      comment.setGroupNo(groupComment);

      //요청에 order 요구사항이 없다면 숫자 센 다음 +1
      if (Objects.isNull(request.getGroupOrder())) {
        Integer currentOrderNo = commentRepository.countChildByGroupNo(
            groupComment.getRealGroupNo());
        comment.setGroupOrder(currentOrderNo + 1);
      }

      //요청에 order 가 들어있다면
      if (Objects.nonNull(request.getGroupOrder())) {
        //그데로 넣어주고
        comment.setGroupOrder(request.getGroupOrder());

        //상위 order 전부 +1해서 수정 저장
        commentRepository.updateUpperCommentByGroupNoCommentId(request.getGroupCommentNo(),
            request.getGroupOrder());
      }
    }
    //요청에 그룹번호가 없는 아예 첫 게시글이라면 그룹번호 지정 후 오더 0으로 설정
    if (request.getGroupCommentNo() == null) {
      Integer baseGroupOrder = 0;
      comment.setGroupOrder(baseGroupOrder);
      Comment saveForGroupNo = commentRepository.save(comment);
      comment.setGroupNo(saveForGroupNo);
    }

    Comment savedComment = commentRepository.save(comment);

    return savedComment.getPostId().getPostId();
  }

  @Override
  public Long updateComment(CommentRequest request) {
    Comment original = commentRepository.findById(request.getPostId())
        .orElseThrow(() -> new NotFoundException(Comment.class, "comment not found"));

    original.setContent(request.getContent());

    Comment saved = commentRepository.save(original);
    return saved.getCommentId();
  }

  @Override
  public void deleteComment(Long commentId, Long memberNo) {
    Comment original = commentRepository.findById(commentId)
        .orElseThrow(() -> new NotFoundException(Comment.class, "comment not found"));

    if(original.getMemberId().getMemberNo().equals(memberNo)) {
      commentRepository.softDelete(commentId);
    }
  }
}
