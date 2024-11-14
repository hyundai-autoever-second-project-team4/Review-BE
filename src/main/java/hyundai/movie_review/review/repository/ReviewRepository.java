package hyundai.movie_review.review.repository;

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

    @Query("SELECT r FROM Review r LEFT JOIN r.thearUps t WHERE r.movie.id = :movieId AND r.deleted = false "
            + "GROUP BY r.id ORDER BY COUNT(t) DESC")
    List<Review> findByMovieId(@Param("movieId") Long movieId, Pageable pageable);
}
