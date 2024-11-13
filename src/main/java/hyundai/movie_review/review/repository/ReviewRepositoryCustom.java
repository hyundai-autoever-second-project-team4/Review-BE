package hyundai.movie_review.review.repository;

import hyundai.movie_review.review.dto.ReviewCountListDto;

public interface ReviewRepositoryCustom {

    ReviewCountListDto getReviewCountsByMovieId(Long movieId);

}
