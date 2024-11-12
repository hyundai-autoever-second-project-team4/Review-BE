package hyundai.movie_review.review.repository;

import hyundai.movie_review.review.dto.ReviewCountDto;
import hyundai.movie_review.review.dto.ReviewCountListDto;
import java.util.List;

public interface ReviewRepositoryCustom {

    ReviewCountListDto getReviewCountsByMovieId(Long movieId);

}
