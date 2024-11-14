package hyundai.movie_review.tier.dto;

import hyundai.movie_review.tier.entity.Tier;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "마이페이지에 필요한 티어 정보 DTO")
public record TierMyPageInfoDto (
        @Schema(description = "티어 ID", example = "1")
        Long tierId,
        @Schema(description = "티어 이름", example = "띠어력 신입")
        String tierName,
        @Schema(description = "티어 이미지 경로", example = "http://k.kakaocdn.net/...")
        String tierImage,
        @Schema(description = "현재 티어에서 다음 티어까지 필요한 총 점수", example = "100")
        Long tierRequiredPoints,
        @Schema(description = "사용자의 점수", example = "30")
        Long tierCurrentPoints
){
    public static TierMyPageInfoDto of(Tier tier, Long score){
        long requiredPoints = 0, currentPoints = 0;

        // Lv 1 ~ 3은 다음 티어/레벨까지 100 포인트 필요
        if(tier.getId() == 1){ requiredPoints = 100; currentPoints = score; }
        else if(tier.getId() == 2){ requiredPoints = 100; currentPoints = score-101; }
        else if(tier.getId() == 3){ requiredPoints = 100; currentPoints = score-201; }
        // Lv 4 ~ 5은 다음 티어/레벨까지 200 포인트 필요
        else if(tier.getId() == 4){ requiredPoints = 200; currentPoints = score-301; }
        else if(tier.getId() == 5){ requiredPoints = 200; currentPoints = score-501; }
        // Lv 6은 다음 티어/레벨까지 300 포인트 필요
        else if(tier.getId() == 6){ requiredPoints = 300; currentPoints = score-701; }
        // Lv 7 ~ 8은 다음 티어/레벨까지 500 포인트 필요
        else if(tier.getId() == 7){ requiredPoints = 500; currentPoints = score-1001; }
        else if(tier.getId() == 8){ requiredPoints = 500; currentPoints = score-1501; }
        // Lv 9은 다음 티어/레벨까지 3000 포인트 필요
        else if(tier.getId() == 9){ requiredPoints = 3000; currentPoints = score-2001; }
        // Lv 10이 마지막
        else { requiredPoints = 3000; currentPoints = requiredPoints; }

        return new TierMyPageInfoDto(
                tier.getId(),
                tier.getName(),
                tier.getImage(),
                requiredPoints,
                currentPoints
        );
    }
}
