package hyundai.movie_review.movie.repository;

import hyundai.movie_review.movie.entity.Movie;
import java.util.List;

public interface MovieRepositoryCustom {
    List<Movie> findMoviesByHighestRatingThisWeek();

}
