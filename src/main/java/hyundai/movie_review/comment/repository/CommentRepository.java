package hyundai.movie_review.comment.repository;

import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByReviewId(Review review, Pageable pageable);
}
