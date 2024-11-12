package hyundai.movie_review.movie.service;

import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.exception.MovieIdNotFoundException;
import hyundai.movie_review.movie.repository.MovieRepository;
import hyundai.movie_review.review.dto.ReviewCountDto;
import hyundai.movie_review.review.dto.ReviewCountListDto;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.repository.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    public MovieDetailResponse getMovieDetail(Long movieId) {
        // 1) 영화 id에 해당하는 영화 정보 조회
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(MovieIdNotFoundException::new);
        // 2) 영화 id에 해당하는 리뷰 조회
        ReviewCountListDto reviewCountListDto = ReviewCountListDto.of(
                reviewRepository.getReviewCountsByMovieId(movieId));

        return MovieDetailResponse.of(movie, reviewCountListDto);
    }

    private void validateMovieExists(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieIdNotFoundException();
        }
    }
}
