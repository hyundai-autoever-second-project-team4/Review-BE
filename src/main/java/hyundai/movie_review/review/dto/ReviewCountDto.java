package hyundai.movie_review.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "특정 평점의 리뷰 개수를 나타내는 DTO")
public record ReviewCountDto(

        @Schema(description = "별점 (예: 0.5, 1.0, 1.5, ...)", example = "4.5")
        double starRate,

        @Schema(description = "해당 별점에 대한 리뷰 개수", example = "42")
        long count
) {

    public static ReviewCountDto of(double starRate, long count) {
        return new ReviewCountDto(starRate, count);
    }

}
