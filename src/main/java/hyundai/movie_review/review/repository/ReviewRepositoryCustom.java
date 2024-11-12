package hyundai.movie_review.review.repository;

import hyundai.movie_review.review.dto.ReviewCountDto;
import java.util.List;

public interface ReviewRepositoryCustom {

    List<ReviewCountDto> getReviewCountsByMovieId(Long movieId);

}
