package hyundai.movie_review.badge.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record BadgeMyPageInfoDto(
        @Schema(description = "대표 배지 ID", example = "1")
        Long badgeId,

        @Schema(description = "대표 배지 설정 시 사용자 배경 이미지 경로")
        String badgeBackgroundImg,

        @Schema(description = "배지 목록")
        List<BadgeInfoDto> badges
) {
}
