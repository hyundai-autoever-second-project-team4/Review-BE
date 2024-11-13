package hyundai.movie_review.member.dto;

import hyundai.movie_review.badge.dto.BadgeInfoDto;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.tier.dto.TierInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberInfoResponse(
        @Schema(description = "회원 ID", example = "1")
        long id,

        @Schema(description = "회원 이메일", example = "user@example.com")
        String email,

        @Schema(description = "회원 이름", example = "John Doe")
        String name,

        @Schema(description = "회원 프로필 이미지 URL", example = "http://example.com/profile.jpg")
        String profileImage,

        @Schema(description = "소셜 로그인 제공자", example = "Google")
        String social,

        @Schema(description = "회원의 배지 정보")
        BadgeInfoDto badge,

        @Schema(description = "회원의 티어 정보")
        TierInfoDto tier
) {

    public static MemberInfoResponse of(Member member) {
        return new MemberInfoResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getProfileImage(),
                member.getSocial(),
                BadgeInfoDto.of(member.getBadge()),
                TierInfoDto.of(member.getTier())
        );
    }

}
