package hyundai.movie_review.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "영화 리뷰 요약 정보: 전체 리뷰 개수, 평균 평점, 평점별 리뷰 개수 목록을 포함")
public record ReviewCountListDto(

        @Schema(description = "전체 리뷰 개수", example = "100")
        long totalReviewCount,

        @Schema(description = "영화의 평균 평점", example = "4.2")
        double averageStarRate,

        @Schema(description = "각 평점별 리뷰 개수 목록 (예: 0.5점, 1점 등)")
        List<ReviewCountDto> reviewCounts
) {

    public static ReviewCountListDto of(
            long totalReviewCount,
            double averageStarRate,
            List<ReviewCountDto> reviewCountDtos) {
        return new ReviewCountListDto(
                totalReviewCount,
                averageStarRate,
                reviewCountDtos);
    }

}
