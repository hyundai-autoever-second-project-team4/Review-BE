package hyundai.movie_review.comment.service;

import hyundai.movie_review.comment.dto.CommentCreateRequest;
import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.comment.exception.CommentIdNotFoundException;
import hyundai.movie_review.comment.exception.CommentMemberIdValidationException;
import hyundai.movie_review.comment.repository.CommentRepository;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.exception.MemberEmailNotFoundException;
import hyundai.movie_review.security.MemberResolver;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberResolver memberResolver;

    public Comment createComment(CommentCreateRequest request) {
        // 1) 현재 api를 사용하는 멤버 정보를 가져온다.
        Member member = memberResolver.getCurrentMember();

        // 2) request + 현재 요청한 멤버 = comment
        Comment comment = Comment.builder()
                .memberId(member.getId())
                .reviewId(request.reviewId())
                .content(request.content())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 3) repository (db)에 저장
        Comment savedComment = commentRepository.save(comment);

        log.info("comment 생성 완료 , 멤버 이름 : {}, 코멘트 넘버 {}", member.getName(), savedComment.getId());

        // 추가) comment entity -> dto로 변환하는 작업

        return savedComment;
    }

    public void deleteComment(Long commentId) {
        // 1) 현재 멤버 정보가 있는 지 확인
        Member member = memberResolver.getCurrentMember();

        // 2) comment db에서 해당 commentId가 있는 지 검증
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentIdNotFoundException::new);

        // 3) comment의 작성자가 현재 접속한 멤버와 같은 지
        if (member.getId() != comment.getMemberId()) {
            throw new CommentMemberIdValidationException();
        }

        commentRepository.delete(comment);

        log.info("코멘트 삭제 완료!");
    }
}
