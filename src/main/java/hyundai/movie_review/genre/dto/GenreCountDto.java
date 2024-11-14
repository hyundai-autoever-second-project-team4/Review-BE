package hyundai.movie_review.genre.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record GenreCountDto(
        @Schema(description = "장르 이름")
        String name,

        @Schema(description = "장르 본 횟수")
        Long count
) {
}
