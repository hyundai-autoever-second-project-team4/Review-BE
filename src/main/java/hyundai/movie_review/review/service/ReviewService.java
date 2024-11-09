package hyundai.movie_review.review.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.dto.ReviewCreateRequest;
import hyundai.movie_review.review.dto.ReviewCreateResponse;
import hyundai.movie_review.review.entity.Review;
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
}
