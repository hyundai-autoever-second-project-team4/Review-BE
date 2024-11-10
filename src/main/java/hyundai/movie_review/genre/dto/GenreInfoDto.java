package hyundai.movie_review.genre.dto;

import hyundai.movie_review.genre.entity.Genre;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "장르 정보 DTO")
public record GenreInfoDto(
        @Schema(description = "장르 ID", example = "1")
        long genreId,
        @Schema(description = "장르 이름", example = "액션")
        String name
) {

    public static GenreInfoDto of(Genre genre) {
        return new GenreInfoDto(
                genre.getId(),
                genre.getName()
        );
    }

}
