package hyundai.movie_review.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마이페이지 별점 분포 정보 : 선택한 평점 개수 배열, 평균 평점, 전체 평점 개수, 가장 많이 선택한 평점")
public record ReviewCountArrayDto(
        @Schema(description = "선택한 평점 개수 배열", example = "[0,0,1,0,1,0,0,1,2,3]")
        long[] starRate,

        @Schema(description = "평균 평점", example = "4.0")
        double averageRate,

        @Schema(description = "전체 평점 개수", example = "8")
        long totalRateCount,

        @Schema(description = "가장 많은 평점", example = "5.0")
        double mostRated
) {

}
