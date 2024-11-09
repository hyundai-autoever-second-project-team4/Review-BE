package hyundai.movie_review.review.repository;

import hyundai.movie_review.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
