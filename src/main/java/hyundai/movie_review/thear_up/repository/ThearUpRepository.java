package hyundai.movie_review.thear_up.repository;

import hyundai.movie_review.thear_up.entity.ThearUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThearUpRepository extends JpaRepository<ThearUp,Long> {
    boolean existsByMemberIdAndReviewId(Long memberId, Long reviewId); //멤버 아이디와 리뷰 아이디를 가진 리뷰가 있는지?
    void deleteByMemberIdAndReviewId(Long memberId, Long reviewId); //Up을 삭제하는 메서드
}
