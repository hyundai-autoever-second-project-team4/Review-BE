package hyundai.movie_review.movie.service;

import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.dto.MovieWithRatingListResponse;
import hyundai.movie_review.movie.dto.MovieWithRatingInfoDto;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.exception.MovieIdNotFoundException;
import hyundai.movie_review.movie.repository.MovieRepository;
import hyundai.movie_review.review.dto.ReviewCountListDto;
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
        ReviewCountListDto reviewCountListDto =
                reviewRepository.getReviewCountsByMovieId(movieId);

        return MovieDetailResponse.of(movie, reviewCountListDto);
    }

    public MovieWithRatingListResponse getMovieStarRate() {
        // 1) 이번 주 기준으로 별점 높은 영화들 가져오기
        List<MovieWithRatingInfoDto> movieWithRatingInfoDtos = movieRepository.findMoviesByHighestRatingThisWeek();

        return MovieWithRatingListResponse.of(movieWithRatingInfoDtos);
    }

    public MovieWithRatingListResponse getMostReviewedMoviesThisWeek() {
        List<MovieWithRatingInfoDto> movieWithRatingInfoDtos = movieRepository.findMoviesByMostReviewsThisWeek();

        return MovieWithRatingListResponse.of(movieWithRatingInfoDtos);
    }

}
