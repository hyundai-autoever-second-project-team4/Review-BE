package hyundai.movie_review.movie.dto;

import hyundai.movie_review.movie.entity.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "영화 정보 DTO")
public record MovieInfoDto(

        @Schema(description = "영화 제목", example = "Inception")
        String title,

        @Schema(description = "영화 개요", example = "A mind-bending thriller")
        String overview,

        @Schema(description = "포스터 이미지 경로", example = "/images/inception_poster.jpg")
        String posterPath,

        @Schema(description = "백드롭 이미지 경로", example = "/images/inception_backdrop.jpg")
        String backdropPath,

        @Schema(description = "성인 영화 여부", example = "false")
        boolean adult,

        @Schema(description = "개봉일", example = "2010-07-16T00:00:00")
        LocalDateTime releaseDate,

        @Schema(description = "상영 시간(분)", example = "148")
        int runtime,

        @Schema(description = "제작 국가", example = "USA")
        String originCountry,

        @Schema(description = "총 리뷰 수", example = "1500")
        long totalReviewCount,

        @Schema(description = "총 별점 합계", example = "7500.0")
        double totalStarRate
) {

}
