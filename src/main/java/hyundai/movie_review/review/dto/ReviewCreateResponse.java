package hyundai.movie_review.review.dto;

import hyundai.movie_review.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 생성 응답 데이터")
public record ReviewCreateResponse(

        @Schema(description = "생성된 리뷰의 ID", example = "1")
        Long reviewId,

        @Schema(description = "생성 완료 메시지", example = "리뷰가 성공적으로 생성되었습니다.")
        String message

) {

    @Schema(description = "Review 엔티티를 기반으로 ReviewCreateResponse 생성")
    public static ReviewCreateResponse of(Review review) {
        return new ReviewCreateResponse(review.getId(), "리뷰가 성공적으로 생성되었습니다.");
    }

}
