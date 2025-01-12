package hyundai.movie_review.movie.repository;

import hyundai.movie_review.movie.dto.MovieWithRatingInfoDto;
import hyundai.movie_review.movie.entity.Movie;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MovieRepositoryCustom {

    List<MovieWithRatingInfoDto> findMoviesByHighestRatingThisWeek();

    List<MovieWithRatingInfoDto> findMoviesByMostReviewsThisWeek();

    List<MovieWithRatingInfoDto> findRecommendedMoviesForMemberByGenreId(long genreId, long memberId);

    List<MovieWithRatingInfoDto> findHonorBoardMovies();
    Page<Movie> findMoviesByGenreName(String genreName, Pageable pageable);

}
