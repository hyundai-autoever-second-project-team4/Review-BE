package hyundai.movie_review.badge.dto;

import hyundai.movie_review.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record BadgeMyPageInfoDto(
        @Schema(description = "대표 배지 ID", example = "1")
        Long primaryBadgeId,

        @Schema(description = "대표 배지 설정 시 사용자 배경 이미지 경로", example = "background.img.url")
        String primaryBadgeBackgroundImg,

        @Schema(description = "배지 목록")
        List<BadgeInfoDto> badges
) {
        public static BadgeMyPageInfoDto of(Member member){
                List<BadgeInfoDto> badges = new java.util.ArrayList<>(member.getMemberBadges().stream()
                        .map(memberBadge -> {
                            return BadgeInfoDto.of(memberBadge.getBadgeId());
                        }).toList());

                BadgeInfoDto primaryBadge = BadgeInfoDto.of(member.getBadge());
                if(badges.contains(primaryBadge)) {
                        badges.remove(primaryBadge);
                        badges.add(0, primaryBadge);
                }

                return new BadgeMyPageInfoDto(
                        member.getBadge().getId(),
                        member.getBadge().getBackground_img(),
                        badges
                );
        }
}
