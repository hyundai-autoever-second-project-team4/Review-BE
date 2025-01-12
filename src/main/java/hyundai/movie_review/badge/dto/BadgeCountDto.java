package hyundai.movie_review.badge.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BadgeCountDto(
        @Schema(description = "배지 id", example = "1")
        Long badgeId,

        @Schema(description = "획득한 멤버 수", example = "123")
        Long counts
) {
}
