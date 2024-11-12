package hyundai.movie_review.thear_up.repository;

import hyundai.movie_review.thear_up.entity.ThearUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThearUpRepository extends JpaRepository<ThearUp,Long> {
    boolean existsByMemberIdAndReviewId(Long memberId, Long reviewId); //버튼을 눌렀을 때 true면 false로 바꿔주는 메서드
    void deleteByMemberIdAndReviewId(Long memberId, Long reviewId); //Up을 삭제하는 메서드
}
