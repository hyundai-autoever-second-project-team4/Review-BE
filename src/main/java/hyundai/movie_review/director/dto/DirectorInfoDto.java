package hyundai.movie_review.director.dto;

import hyundai.movie_review.director.entity.Director;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "영화 감독 정보 DTO")
public record DirectorInfoDto(
        @Schema(description = "감독의 ID", example = "1")
        long directorId,

        @Schema(description = "영화의 ID", example = "100")
        long movieId,

        @Schema(description = "감독의 이름", example = "Christopher Nolan")
        String name,

        @Schema(description = "감독의 프로필 이미지 경로", example = "/profiles/nolan.jpg")
        String profilePath
) {

    public static DirectorInfoDto of(Director director) {
        return new DirectorInfoDto(
                director.getId(),
                director.getMovie().getId(),
                director.getName(),
                director.getProfilePath()
        );
    }

}

