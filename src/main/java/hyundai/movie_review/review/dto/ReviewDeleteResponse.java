package hyundai.movie_review.review.dto;

import hyundai.movie_review.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 삭제 응답 데이터")
public record ReviewDeleteResponse(
        @Schema(description = "삭제된 리뷰의 ID", example = "0")
        Long reviewId,

        @Schema(description = "삭제 완료 메시지", example = "리뷰가 삭제되었습니다.")
        String message
) {

    public static ReviewDeleteResponse of(Review review) {
        return new ReviewDeleteResponse(review.getId(), "리뷰가 삭제되었습니다.");
    }

}
