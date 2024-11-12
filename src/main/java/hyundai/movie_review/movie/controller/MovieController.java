package hyundai.movie_review.movie.controller;

import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getMovieDetail(
            @PathVariable Long movieId
    ) {
        MovieDetailResponse response = movieService.getMovieDetail(movieId);

        return ResponseEntity.ok(response);
    }
}
