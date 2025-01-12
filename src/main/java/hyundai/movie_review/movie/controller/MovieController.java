package hyundai.movie_review.movie.controller;

import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.dto.MovieWithRatingListResponse;
import hyundai.movie_review.movie.service.MovieService;
import hyundai.movie_review.review.dto.ReviewInfoPageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
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
                            schema = @Schema(implementation = MovieWithRatingListResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/movie/top-rated/weekly")
    public ResponseEntity<?> getMoviesByHighestRatingThisWeek() {
        MovieWithRatingListResponse response = movieService.getMoviesByHighestRatingThisWeek();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "이번 주 리뷰 많은 영화 조회", description = "이번 주 기준으로 리뷰가 많은 영화 순으로 정렬하여 조회하여 리턴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이번 주 리뷰 순 영화 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieWithRatingListResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/movie/most-reviewed/weekly")
    public ResponseEntity<?> getMostReviewedMoviesThisWeek() {
        MovieWithRatingListResponse response = movieService.getMostReviewedMoviesThisWeek();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용자 맞춤 영화 조회", description = "사용자가 작성한 리뷰의 영화 장르 통계를 통해 사용자 맞춤 영화 정보를 리턴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 맞춤 영화 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieWithRatingListResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/movie/recommend")
    public ResponseEntity<?> getRecommendedMoviesForMember() {
        MovieWithRatingListResponse response = movieService.getRecommendedMoviesForMember();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "명예의 전당 영화 조회", description = "전체 영화들 중, 리뷰가 가장 많고 별점이 높은 명예의 전당 영화 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "명예의 전당 영화 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MovieWithRatingListResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/movie/honor-board")
    public ResponseEntity<?> getHonorBoardMovies() {
        MovieWithRatingListResponse response = movieService.getHonorBoardMovies();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "영화 전체 리뷰 상세 조회",
            description = "영화 id에 해당하는 리뷰 상세 내용을 조회해 페이지네이션으로 전달, "
                    +"type에 likes(좋아요순), latest(최신순), ratingHigh(별점높은순), ratingLow(별점낮은순), comments(댓글많은순)를 받는다.")
    @GetMapping("/movie/{movieId}/reviews")
    public ResponseEntity<?> getMovieReview(
            @PathVariable("movieId") Long movieId,
            @Parameter(example = "likes, latest, ratingHigh, ratingLow, comments 중 택1") @RequestParam(defaultValue = "likes", name = "type") String type,
            @RequestParam(defaultValue = "0", name = "page") Integer page
    ){
        ReviewInfoPageDto response = movieService.getMovieReviewDetail(movieId, type, page);

        return ResponseEntity.ok(response);
    }

}
