package hyundai.movie_review.review.repository;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.review.entity.Review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Optional<Review> findByIdAndDeletedFalse(Long reviewId);

    boolean existsByMemberIdAndMovieIdAndDeletedFalse(Long memberId, Long movieId);

    // 영화 리뷰 개수
    long countByMovieId(Long movieId);

    // 영화 리뷰 최신순
    List<Review> findByMovieIdAndDeletedFalseOrderByCreatedAtDesc(Long movieId, Pageable pageable);

    // 영화 리뷰 up순
    @Query("SELECT r FROM Review r LEFT JOIN r.thearUps t WHERE r.movie.id = :movieId AND r.deleted = false "
            + "GROUP BY r.id ORDER BY COUNT(t) DESC")
    List<Review> findByMovieIdOrderByUps(@Param("movieId") Long movieId, Pageable pageable);

    // 영화 리뷰 별점 많은 순
    List<Review> findByMovieIdAndDeletedFalseOrderByStarRateDesc(Long movieId, Pageable pageable);

    // 영화 리뷰 별점 낮은 순
    List<Review> findByMovieIdAndDeletedFalseOrderByStarRate(Long movieId, Pageable pageable);

    // 영화 리뷰 댓글 많은 순
    @Query("SELECT r FROM Review r LEFT JOIN r.comments c WHERE r.movie.id = :movieId AND r.deleted = false "
            + "GROUP BY r.id ORDER BY COUNT(c) DESC")
    List<Review> findByMovieIdOrderByComments(@Param("movieId") Long movieId, Pageable pageable);

    List<Review> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    // 영화 리뷰 개수
    long countByMember(Member member);

    // 사용자가 작성한 리뷰들 중, 영화 리뷰 최신순
    List<Review> findByMemberAndDeletedFalseOrderByCreatedAtDesc(Member member, Pageable pageable);

    // 사용자가 작성한 리뷰들 중, 영화 리뷰 up순
    @Query("SELECT r FROM Review r LEFT JOIN r.thearUps t WHERE r.member = :member AND r.deleted = false "
            + "GROUP BY r.id ORDER BY COUNT(t) DESC")
    List<Review> findByMemberOrderByUps(@Param("member") Member member, Pageable pageable);

    // 사용자가 작성한 리뷰들 중, 영화 리뷰 별점 많은 순
    List<Review> findByMemberAndDeletedFalseOrderByStarRateDesc(Member member, Pageable pageable);

    // 사용자가 작성한 리뷰들 중, 영화 리뷰 별점 낮은 순
    List<Review> findByMemberAndDeletedFalseOrderByStarRate(Member member, Pageable pageable);

    // 사용자가 작성한 리뷰들 중, 영화 리뷰 댓글 많은 순
    @Query("SELECT r FROM Review r LEFT JOIN r.comments c WHERE r.member = :member AND r.deleted = false "
            + "GROUP BY r.id ORDER BY COUNT(c) DESC")
    List<Review> findByMemberOrderByComments(@Param("member") Member member, Pageable pageable);

}
