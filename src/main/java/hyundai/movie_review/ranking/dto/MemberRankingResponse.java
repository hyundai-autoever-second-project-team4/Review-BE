package hyundai.movie_review.ranking.dto;

import org.springframework.data.domain.Page;

public record MemberRankingResponse(
        Page<MemberRankingInfoDto> memberRankings
) {

    public static MemberRankingResponse of(Page<MemberRankingInfoDto> memberRankingInfoDtos) {
        return new MemberRankingResponse(memberRankingInfoDtos);
    }

}
