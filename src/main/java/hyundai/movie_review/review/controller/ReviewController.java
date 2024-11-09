package hyundai.movie_review.review.controller;

import hyundai.movie_review.exception.BusinessExceptionResponse;
import hyundai.movie_review.review.dto.ReviewCreateRequest;
import hyundai.movie_review.review.dto.ReviewCreateResponse;
import hyundai.movie_review.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 생성", description = "영화에 대한 리뷰를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "리뷰 생성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewCreateResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @PostMapping("/review")
    public ResponseEntity<ReviewCreateResponse> createReview(
            @RequestBody ReviewCreateRequest request
    ) {
        ReviewCreateResponse response = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
