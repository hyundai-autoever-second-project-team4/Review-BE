package hyundai.movie_review.thear_down.repository;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.thear_down.entity.ThearDown;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThearDownRepository extends JpaRepository<ThearDown,Long> {
    boolean existsByMemberIdAndReviewId(Member memberId, Review reviewId);//멤버 아이디와 리뷰 아이디를 가진 리뷰가 있는지?
    void deleteByMemberIdAndReviewId(Member memberId, Review reviewId);//싫어요 삭제하는 메서드
}
