package hyundai.movie_review.review.repository;

import hyundai.movie_review.review.dto.ReviewByMovieIdDto;
import hyundai.movie_review.review.dto.ReviewCountDto;
import hyundai.movie_review.review.dto.ReviewCountListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewRepositoryCustom {

    ReviewCountListDto getReviewCountsByMovieId(Long movieId);

    Page<ReviewByMovieIdDto> getReviewInfosByMovieId(Long movieId, Pageable pageable);
}
