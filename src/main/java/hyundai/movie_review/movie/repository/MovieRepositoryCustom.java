package hyundai.movie_review.movie.repository;

import hyundai.movie_review.movie.dto.MovieDetailResponse;
import java.util.Optional;

public interface MovieRepositoryCustom {

    MovieDetailResponse findMovieDetailById(Long movieId);

}
