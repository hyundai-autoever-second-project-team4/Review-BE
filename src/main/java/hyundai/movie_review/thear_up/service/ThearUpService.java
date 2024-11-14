package hyundai.movie_review.thear_up.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.exception.ReviewIdNotFoundException;
import hyundai.movie_review.review.repository.ReviewRepository;
import hyundai.movie_review.security.MemberResolver;
import hyundai.movie_review.thear_up.dto.ThearUpResponse;
import hyundai.movie_review.thear_up.entity.ThearUp;
import hyundai.movie_review.thear_up.event.ThearUpScoreEvent;
import hyundai.movie_review.thear_up.repository.ThearUpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service //서비스 어노테이션
@RequiredArgsConstructor // final을 쓸 때 자동으로 생성자를 만들어줌
@Slf4j
public class ThearUpService {

    private final ThearUpRepository thearUpRepository;
    private final ReviewRepository reviewRepository;
    private final MemberResolver memberResolver;
    private final ApplicationEventPublisher applicationEventPublisher;

    //1. 지금 좋아요를 누른 맴버의 아이디와 리뷰의 맴버 아이디가 같은 것을 찾기?
    //2. 좋아요를 처리해주는데 만약 좋아요가 없었다면 true로 처리
    //3. 좋아요를 처리해주는데 이미 좋아요가 눌러있었으면 false로 처리
    @Transactional
    // 데이터베이스 작업을 트랜잭션으로 묶어줍니다. 트랜잭션은 일련의 작업이 모두 성공적으로 완료되거나, 하나라도 실패할 경우 모든 작업이 롤백되는 것을 보장합니다.
    public ThearUpResponse toggleThearUp(Long reviewId) {

        // 1) 현재 로그인한 멤버를 가져오기.
        Member currentMember = memberResolver.getCurrentMember();

        // 2) 리뷰 가져오기
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewIdNotFoundException::new);

        /* TODO
            전제] 현재 멤버 : 1, 리뷰 : 1
            1) 내가 1번이고, 리뷰 1번에 ThearUpRepository에 해당 record가 있으면 -> thearup 삭제
            2) 내가 1번이고, 리뷰 1번에 ThearUpRepository에 해당 record가 없으면 -> thearup 생성

            * ThearupRepository에서 memberId, reviewId에 해당하는 레코드가 있는 지 검사 -> boolean
        * */

        // 이미 좋아요가 있는 경우 -> exists(true)
        if (thearUpRepository.existsByMemberIdAndReviewId(currentMember, review)) {
            // 이미 좋아요가 있으면 삭제 (좋아요 취소)
            thearUpRepository.deleteByMemberIdAndReviewId(currentMember, review);

            log.info("좋아요 삭제 완료");

            // 리뷰 작성자에게 포인트를 줘야 하므로, 리뷰 작성자를 전달
            applicationEventPublisher.publishEvent(
                    new ThearUpScoreEvent(this, review.getMember(), false));

            return ThearUpResponse.of("좋아요 삭제 완료");
        } else {
            // 좋아요가 없으면 새로 생성
            ThearUp thearUp = ThearUp.builder()
                    .memberId(currentMember)
                    .reviewId(review)
                    .build();

            thearUpRepository.save(thearUp);

            log.info("좋아요 생성 완료");

            // 리뷰 작성자에게 포인트를 줘야 하므로, 리뷰 작성자를 전달
            applicationEventPublisher.publishEvent(
                    new ThearUpScoreEvent(this, review.getMember(), true));

            return ThearUpResponse.of("좋아요 생성 완료");
        }
    }
}
