package hyundai.movie_review.review.repository;

import hyundai.movie_review.review.dto.ReviewInfoListDto;
import hyundai.movie_review.review.dto.ReviewCountListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {

    ReviewCountListDto getReviewCountsByMovieId(Long movieId);

}
