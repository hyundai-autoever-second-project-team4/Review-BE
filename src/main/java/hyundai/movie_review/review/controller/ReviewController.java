package hyundai.movie_review.review.controller;

import hyundai.movie_review.exception.BusinessExceptionResponse;
import hyundai.movie_review.review.dto.ReviewCreateRequest;
import hyundai.movie_review.review.dto.ReviewCreateResponse;
import hyundai.movie_review.review.dto.ReviewDeleteResponse;
import hyundai.movie_review.review.dto.ReviewInfoListDto;
import hyundai.movie_review.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review", description = "리뷰 관련 API")
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
            @ApiResponse(responseCode = "401", description = "사용자 인증 오류",
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

    @Operation(summary = "리뷰 삭제", description = "영화에 대해 작성한 리뷰를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 삭제 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDeleteResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "401", description = "사용자 인증 오류",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "403", description = "사용자 권한 오류 (다른 사용자)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BusinessExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @Parameter(description = "삭제할 리뷰의 고유 ID", required = true)
            @PathVariable Long reviewId
    ) {
        ReviewDeleteResponse response = reviewService.deleteReview(reviewId);

        return ResponseEntity.ok(response);

    }

    @Operation(summary = "오늘 HOT한 리뷰 조회", description = "오늘 작성한 리뷰들 중, 좋아요를 많이 받은 순으로 정렬하여 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "HOT 리뷰 조회 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReviewInfoListDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/review/hot")
    public ResponseEntity<?> getHotReviews() {
        ReviewInfoListDto response = reviewService.getHotReviews();

        return ResponseEntity.ok(response);
    }
}
