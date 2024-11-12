package hyundai.movie_review.review.repository;

import hyundai.movie_review.review.entity.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Optional<Review> findByIdAndDeletedFalse(Long reviewId);

    boolean existsByMemberIdAndMovieId(Long memberId, Long movieId);
}
