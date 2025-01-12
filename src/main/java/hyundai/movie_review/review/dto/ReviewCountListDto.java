package hyundai.movie_review.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            List<ReviewCountDto> actualReviewCounts) {

        // 0.5부터 5.0까지의 기본 평점 리스트 생성
        List<Double> ratings = List.of(0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0);

        // 평점별 실제 리뷰 개수를 Map으로 변환
        Map<Double, Long> reviewCountMap = actualReviewCounts.stream()
                .collect(Collectors.toMap(ReviewCountDto::starRate, ReviewCountDto::count));

        // 각 평점에 대한 리뷰 개수 설정 (없다면 0, 있다면 실제 개수)
        List<ReviewCountDto> completeReviewCounts = new ArrayList<>();
        for (Double rating : ratings) {
            completeReviewCounts.add(new ReviewCountDto(
                    rating,
                    reviewCountMap.getOrDefault(rating, 0L)
            ));
        }

        return new ReviewCountListDto(
                totalReviewCount,
                averageStarRate,
                completeReviewCounts
        );
    }
}
