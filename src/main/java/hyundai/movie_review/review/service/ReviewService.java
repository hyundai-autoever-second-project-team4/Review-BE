package hyundai.movie_review.review.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.dto.ReviewCreateRequest;
import hyundai.movie_review.review.dto.ReviewCreateResponse;
import hyundai.movie_review.review.dto.ReviewDeleteResponse;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.exception.ReviewAuthorMismatchException;
import hyundai.movie_review.review.exception.ReviewIdNotFoundException;
import hyundai.movie_review.review.repository.ReviewRepository;
import hyundai.movie_review.security.MemberResolver;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberResolver memberResolver;

    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest request) {
        // 1) 현재 로그인 한 멤버를 가져온다.
        Member currentMember = memberResolver.getCurrentMember();

        /* TODO
         *   request.movieId가 db에 존재하는 지 검증하기 */

        // 2) 멤버 정보를 이용하여 Review Entity 생성
        Review review = Review.builder()
                .movieId(request.movieId())
                .memberId(currentMember.getId())
                .starRate(request.starRate())
                .content(request.content())
                .spoiler(request.spoiler())
                .createdAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);

        /* TODO
         *   영화 리뷰 정보를 영화 db에 반영하기
         *       1) 영화 db의 total Review count 증가
         *       2)  영화 db의 누적 review star rate 증가 */

        log.info("Review 생성 성공 : {}", savedReview.getId());

        return ReviewCreateResponse.of(savedReview);
    }

    @Transactional
    public ReviewDeleteResponse deleteReview(Long reviewId) {
        // 1) 현재 로그인 한 멤버를 가져온다.
        Member currentMember = memberResolver.getCurrentMember();

        // 2) reviewId에 해당하는 review entity를 가져온다.
        Review review = reviewRepository.findByIdAndDeletedFalse(reviewId)
                .orElseThrow(ReviewIdNotFoundException::new);

        // 3) 해당 멤버가 리뷰 작성자인지 확인
        validateReviewAuthor(currentMember.getId(), review.getMemberId());

        // 4) 리뷰 상태를 삭제 상태로 변경
        review.delete();

        /* TODO
         *   해당 review의 movieId에 해당하는 movie의 정보 변경하기
         *       movie의 total review count를 하나 제거
         *       movie의 total star rate를 review의 star rate 만큼 뺴기
         * */

        // 5) 삭제 사항 반영
        reviewRepository.save(review);

        return ReviewDeleteResponse.of(review);
    }

    private void validateReviewAuthor(Long memberId, Long reviewerId) {
        if (!memberId.equals(reviewerId)) {
            throw new ReviewAuthorMismatchException();
        }
    }
}
