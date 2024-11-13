package hyundai.movie_review.movie.controller;

import hyundai.movie_review.exception.BusinessExceptionResponse;
import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.dto.MovieListResponse;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.service.MovieService;
import hyundai.movie_review.review.dto.ReviewCreateResponse;
import hyundai.movie_review.review.dto.ReviewDeleteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Movie", description = "영화 관련 API")
public class MovieController {

    private final MovieService movieService;

    @Operation(summary = "영화 상세 조회", description = "특정 영화의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "영화 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieDetailResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<?> getMovieDetail(
            @Parameter(description = "영화 ID", required = true) @PathVariable Long movieId
    ) {
        MovieDetailResponse response = movieService.getMovieDetail(movieId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이번 주 별점 높은 영화 조회", description = "이번 주 기준으로 별점이 높은 영화 순으로 정렬하여 조회하여 리턴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이번 주 별점 순 영화 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieListResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/movie/top-rated/weekly")
    public ResponseEntity<?> getHotStarRate() {
        MovieListResponse response = movieService.getMovieStarRate();

        return ResponseEntity.ok(response);
    }


}
