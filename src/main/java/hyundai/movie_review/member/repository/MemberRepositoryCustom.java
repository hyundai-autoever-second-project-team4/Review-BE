package hyundai.movie_review.member.repository;

import hyundai.movie_review.member.dto.MemberBadgeAndTierDto;

public interface MemberRepositoryCustom {
    MemberBadgeAndTierDto getTierAndBadgeImgByMemberId(Long id);
}
