package hyundai.movie_review.movie.controller;

import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.service.MovieService;
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
            , @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        MovieDetailResponse response = movieService.getMovieDetail(movieId, pageable);
        return ResponseEntity.ok(response);
    }
}
