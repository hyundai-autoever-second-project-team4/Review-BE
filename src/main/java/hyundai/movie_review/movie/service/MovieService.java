package hyundai.movie_review.movie.service;

import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.exception.MovieIdNotFoundException;
import hyundai.movie_review.movie.repository.MovieRepository;
import hyundai.movie_review.movie.repository.MovieRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final MovieRepositoryCustom movieRepositoryCustom;

    public MovieDetailResponse getMovieDetail(Long movieId) {
        // 1) movieId가 db에 존재하는 지 확인
        validateMovieExists(movieId);

        return movieRepositoryCustom.findMovieDetailById(movieId);
    }

    private void validateMovieExists(Long movieId) {
        if (!movieRepository.existsByMovieId(movieId)) {
            throw new MovieIdNotFoundException();
        }
    }
}
