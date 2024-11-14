package hyundai.movie_review.review.repository;

import hyundai.movie_review.review.dto.ReviewCountListDto;
import hyundai.movie_review.review.dto.ReviewInfoListDto;
import hyundai.movie_review.review.entity.Review;
import java.util.List;

public interface ReviewRepositoryCustom {

    ReviewCountListDto getReviewCountsByMovieId(Long movieId);

    List<Review> getReviewsOrderByThearUpCountDesc();

}
