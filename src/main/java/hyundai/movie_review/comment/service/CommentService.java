package hyundai.movie_review.comment.service;

import hyundai.movie_review.comment.dto.*;
import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.comment.event.CommentScoreEvent;
import hyundai.movie_review.comment.exception.CommentIdNotFoundException;
import hyundai.movie_review.comment.exception.CommentMemberIdValidationException;
import hyundai.movie_review.comment.exception.MemberIdNotFoundException;
import hyundai.movie_review.comment.exception.ReviewIdNotFoundException;
import hyundai.movie_review.comment.repository.CommentRepository;
import hyundai.movie_review.member.dto.MemberBadgeAndTierDto;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.exception.MemberEmailNotFoundException;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.repository.ReviewRepository;
import hyundai.movie_review.security.MemberResolver;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final MemberResolver memberResolver;
    private final ApplicationEventPublisher applicationEventPublisher;

    public CommentCreateResponse createComment(CommentCreateRequest request) {
        // 1) 현재 api를 사용하는 멤버 정보를 가져온다.
        Member member = memberResolver.getCurrentMember();

        // 리뷰 id가 존재하지 않는 경우 exception 발생
        Review review = reviewRepository.findById(request.reviewId())
                .orElseThrow(ReviewIdNotFoundException::new);

        // 2) request + 현재 요청한 멤버 = comment
        Comment comment = Comment.builder()
                .memberId(member)
                .reviewId(review)
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 3) repository (db)에 저장
        Comment savedComment = commentRepository.save(comment);

        log.info("comment 생성 완료 , 멤버 이름 : {}, 코멘트 넘버 {}", member.getName(), savedComment.getId());

        // 4) 생성된 comment에 대한 event 처리
        applicationEventPublisher.publishEvent(new CommentScoreEvent(this, member, true));

        return new CommentCreateResponse(
                savedComment.getMemberId().getId(),
                savedComment.getReviewId().getId(),
                savedComment.getContent(),
                savedComment.getCreatedAt()
        );
    }

    public CommentUpdateResponse updateComment(CommentUpdateRequest request) {
        Member member = memberResolver.getCurrentMember();

        Comment comment = commentRepository.findById(request.commentId())
                .orElseThrow(CommentIdNotFoundException::new);

        if (member.getId() != comment.getMemberId().getId()) {
            throw new CommentMemberIdValidationException();
        }

        comment.changeContent(request.content());
        Comment savedComment = commentRepository.save(comment);

        log.info("comment 수정 완료 , 멤버 이름 : {}, 코멘트 넘버 {}, 수정된 코멘트 {}"
                , member.getName(), savedComment.getId(), savedComment.getContent());

        return new CommentUpdateResponse(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getUpdatedAt()
        );
    }

    public void deleteComment(Long commentId) {
        // 1) 현재 멤버 정보가 있는 지 확인
        Member member = memberResolver.getCurrentMember();

        // 2) comment db에서 해당 commentId가 있는 지 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentIdNotFoundException::new);

        // 3) comment의 작성자가 현재 접속한 멤버와 같은 지
        if (member.getId() != comment.getMemberId().getId()) {
            throw new CommentMemberIdValidationException();
        }

        commentRepository.delete(comment);

        applicationEventPublisher.publishEvent(new CommentScoreEvent(this, member, false));

        log.info("코멘트 삭제 완료!");
    }

    // 한 리뷰의 전체 댓글
    public CommentGetAllResponse getAllComments(Long reviewId, Integer page) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewIdNotFoundException::new);

        PageRequest pageRequest = PageRequest.of(page, 10,
                Sort.by(Sort.Direction.ASC, "createdAt"));
        Page<Comment> commentList = commentRepository.findByReviewId(review, pageRequest);

        List<CommentGetResponse> comments = commentList.getContent().stream()
                .map(
                        comment -> {
                            Member member = memberRepository.findById(comment.getMemberId().getId())
                                    .orElseThrow(MemberIdNotFoundException::new);

                            MemberBadgeAndTierDto dto = memberRepository.getTierAndBadgeImgByMemberId(
                                    member.getId());
                            log.info("{}",
                                    memberRepository.getTierAndBadgeImgByMemberId(member.getId()));
                            return CommentGetResponse.of(member, reviewId, comment);
                        }
                ).toList();

        return new CommentGetAllResponse(
                commentList.getTotalElements(),
                comments
        );

    }

    // 특정 댓글 get
    public CommentGetResponse getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentIdNotFoundException::new);

        Member member = memberRepository.findById(comment.getMemberId().getId())
                .orElseThrow();
        log.info("{}", memberRepository.getTierAndBadgeImgByMemberId(member.getId()));
        MemberBadgeAndTierDto dto = memberRepository.getTierAndBadgeImgByMemberId(member.getId());

        return CommentGetResponse.of(member, comment.getReviewId().getId(), comment);
    }
}
