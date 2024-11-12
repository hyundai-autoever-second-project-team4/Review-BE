package hyundai.movie_review.thear_up.service;

import hyundai.movie_review.thear_up.entity.ThearUp;
import hyundai.movie_review.thear_up.repository.ThearUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service //서비스 어노테이션
@RequiredArgsConstructor // final을 쓸 때 자동으로 생성자를 만들어줌
public class ThearUpService {
    private final ThearUpRepository thearUpRepository;
    //1. 지금 좋아요를 누른 맴버의 아이디와 리뷰의 맴버 아이디가 같은 것을 찾기?
    //2. 좋아요를 처리해주는데 만약 좋아요가 없었다면 true로 처리
    //3. 좋아요를 처리해주는데 이미 좋아요가 눌러있었으면 false로 처리
    @Transactional // 데이터베이스 작업을 트랜잭션으로 묶어줍니다. 트랜잭션은 일련의 작업이 모두 성공적으로 완료되거나, 하나라도 실패할 경우 모든 작업이 롤백되는 것을 보장합니다.
    public boolean addThearUp(Long memberId, Long reviewId) {
        if (thearUpRepository.existsByMemberIdAndReviewId(memberId, reviewId)) {
            // 이미 좋아요가 있으면 삭제 (좋아요 취소)
            thearUpRepository.deleteByMemberIdAndReviewId(memberId, reviewId);
            System.out.println("좋아요 삭제 완료");
            return false;
        } else {
            // 좋아요가 없으면 새로 생성
            ThearUp thearUp = new ThearUp();
            thearUp.setMemberId(memberId); //롬북이 만들어준 게터 세터
            thearUp.setReviewId(reviewId);
            thearUpRepository.save(thearUp);
            return true; // 좋아요 추가
        }
    }
}
