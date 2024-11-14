package hyundai.movie_review.member.dto;

import hyundai.movie_review.badge.dto.BadgeMyPageInfoDto;
import hyundai.movie_review.genre.dto.GenreCountListDto;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.tier.dto.TierMyPageInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;

public record GetMemberMyPageResponse(
   @Schema(description = "로그인한 멤버 이름")
   String memberName,

   @Schema(description = "로그인한 멤버 프로필 이미지 경로")
   String memberProfileImg,

   @Schema(description = "멤버의 배지 정보")
   BadgeMyPageInfoDto memberBadgeList,

   @Schema(description = "멤버의 티어 정보")
   TierMyPageInfoDto memberTier,

   @Schema(description = "멤버의 선호 장르 정보")
   GenreCountListDto genreList
) {
   public static GetMemberMyPageResponse of(Member member, GenreCountListDto genreList){

      return new GetMemberMyPageResponse(
              member.getName(),
              member.getProfileImage(),
              BadgeMyPageInfoDto.of(member),
              TierMyPageInfoDto.of(member.getTier(), member.getTotalScore()),
              genreList
      );
   }
}
