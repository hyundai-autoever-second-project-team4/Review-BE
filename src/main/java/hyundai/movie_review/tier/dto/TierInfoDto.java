package hyundai.movie_review.tier.dto;

import hyundai.movie_review.tier.entity.Tier;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "티어 정보 DTO")
public record TierInfoDto(
        @Schema(description = "티어 ID", example = "1")
        Long tierId,
        @Schema(description = "티어 이름", example = "띠어력 신입")
        String name,
        @Schema(description = "티어 이미지 경로")
        String image
) {

    public static TierInfoDto of(Tier tier) {
        return new TierInfoDto(
                tier.getId(),
                tier.getName(),
                tier.getImage()
        );
    }
}
