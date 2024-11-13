package hyundai.movie_review.movie.dto;

import hyundai.movie_review.movie.entity.Movie;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record MovieWithRatingInfoDto(
        @Schema(description = "영화 id", example = "1")
        long movieId,

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

        @Schema(description = "평균 별점", example = "3.5")
        double averageStarRate
) {

    public MovieWithRatingInfoDto(
            long movieId,
            String title,
            String overview,
            String posterPath,
            String backdropPath,
            boolean adult,
            LocalDateTime releaseDate,
            int runtime,
            String originCountry,
            long totalReviewCount,
            double averageStarRate) {
        this.movieId = movieId;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.originCountry = originCountry;
        this.totalReviewCount = totalReviewCount;
        this.averageStarRate = Math.round(averageStarRate * 10) / 10.0;
    }


}
