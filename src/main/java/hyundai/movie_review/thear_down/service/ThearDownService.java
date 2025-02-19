package hyundai.movie_review.thear_down.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.exception.ReviewIdNotFoundException;
import hyundai.movie_review.review.repository.ReviewRepository;
import hyundai.movie_review.security.MemberResolver;
import hyundai.movie_review.thear_down.dto.ThearDownResponse;
import hyundai.movie_review.thear_down.entity.ThearDown;
import hyundai.movie_review.thear_down.event.ThearDownScoreEvent;
import hyundai.movie_review.thear_down.repository.ThearDownRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThearDownService {

    private final ThearDownRepository thearDownRepository;
    private final ReviewRepository reviewRepository;
    private final MemberResolver memberResolver;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Transactional
// 데이터베이스 작업을 트랜잭션으로 묶는다. 일련의 작업이 모두 성공적으로 완료되거나, 하나라도 실패할 경우 모든 작업이 롤백되는 것을 보장합니다.
    public ThearDownResponse toggleThearDown(Long reviewId) {
        //현재 로그인 한 맴버 아이디 가져오기.
        Member currentMember = memberResolver.getCurrentMember();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewIdNotFoundException::new);

        //이미 싫어요가 있을 경우
        if (thearDownRepository.existsByMemberIdAndReviewId(currentMember, review)) {
            //싫어요를 삭제 (싫어요 취소)
            thearDownRepository.deleteByMemberIdAndReviewId(currentMember, review);
            log.info("싫어요 삭제 완료");

            applicationEventPublisher.publishEvent(
                    new ThearDownScoreEvent(this, currentMember, review.getMember(), false));
            return ThearDownResponse.of("싫어요 삭제 완료");
        } else {
            //싫어요가 없으면 새로 생성
            ThearDown thearDown = ThearDown.builder()
                    .memberId(currentMember)
                    .reviewId(review)
                    .build();
            thearDownRepository.save(thearDown);

            log.info("싫어요 생성 완료");

            applicationEventPublisher.publishEvent(
                    new ThearDownScoreEvent(this, currentMember, review.getMember(), true));

            return ThearDownResponse.of("싫어요 생성 완료");
        }

    }
}
