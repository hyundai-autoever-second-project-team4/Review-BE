package hyundai.movie_review.movie.repository;

import hyundai.movie_review.movie.dto.MovieWithRatingInfoDto;
import hyundai.movie_review.movie.entity.Movie;
import java.util.List;

public interface MovieRepositoryCustom {

    List<MovieWithRatingInfoDto> findMoviesByHighestRatingThisWeek();

    List<MovieWithRatingInfoDto> findMoviesByMostReviewsThisWeek();

    List<MovieWithRatingInfoDto> findRecommendedMoviesForMemberByGenreId(long genreId);

    List<MovieWithRatingInfoDto> findHonorBoardMovies();

}
