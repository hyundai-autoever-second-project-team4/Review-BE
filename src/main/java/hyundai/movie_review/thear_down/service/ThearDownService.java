package hyundai.movie_review.thear_down.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.security.MemberResolver;
import hyundai.movie_review.thear_down.dto.ThearDownResponse;
import hyundai.movie_review.thear_down.entity.ThearDown;
import hyundai.movie_review.thear_down.repository.ThearDownRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThearDownService {
    private final ThearDownRepository thearDownRepository;
    private final MemberResolver memberResolver;

    @Transactional// 데이터베이스 작업을 트랜잭션으로 묶는다. 일련의 작업이 모두 성공적으로 완료되거나, 하나라도 실패할 경우 모든 작업이 롤백되는 것을 보장합니다.
    public ThearDownResponse toggleThearDown(Long reviewId){
        //현재 로그인 한 맴버 아이디 가져오기.
        Member currenMember = memberResolver.getCurrentMember();
        //이미 싫어요가 있을 경우
        if(thearDownRepository.existsByMemberIdAndReviewId(currenMember.getId(),reviewId)){
            //싫어요를 삭제 (싫어요 취소)
            thearDownRepository.deleteByMemberIdAndReviewId(currenMember.getId(), reviewId);
            log.info("싫어요 삭제 완료");
            return ThearDownResponse.of("싫어요 삭제 완료");
        } else {
            //싫어요가 없으면 새로 생성
            ThearDown thearDown = ThearDown.builder()
                    .memberId(currenMember.getId())
                    .reviewId(reviewId)
                    .build();
            log.info("싫어요 생성 완료");

            thearDownRepository.save(thearDown);
            return ThearDownResponse.of("싫어요 생성 완료");
        }

    }
}
