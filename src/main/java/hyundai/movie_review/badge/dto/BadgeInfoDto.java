package hyundai.movie_review.badge.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BadgeInfoDto(
        @Schema(description = "배지 ID", example = "1")
        Long id,
        @Schema(description = "배지 이름", example = "회원가입을 축하해요")
        String name,
        @Schema(description = "배지 이미지 경로")
        String image,
        @Schema(description = "대표 배지 설정 시 사용자 배경 이미지 경로")
        String background_image
) {
}
