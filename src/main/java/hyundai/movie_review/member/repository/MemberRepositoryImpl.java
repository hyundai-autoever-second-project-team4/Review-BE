package hyundai.movie_review.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.member.dto.MemberBadgeAndTierDto;
import static hyundai.movie_review.badge.entity.QBadge.badge;
import static hyundai.movie_review.member.entity.QMember.member;
import static hyundai.movie_review.member_badge.entity.QMemberBadge.memberBadge;
import static hyundai.movie_review.tier.entity.QTier.tier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    // 멤버 티어와 대표 뱃지 이미지 경로 dto
    @Override
    public MemberBadgeAndTierDto getTierAndBadgeImgByMemberId(Long id) {
        return jpaQueryFactory.select(Projections.constructor(MemberBadgeAndTierDto.class,
                badge.image,
                tier.image))
                .from(member)
                .join(badge).on(member.badgeId.eq(badge.id))
                .join(member.tierId, tier)
                .where(member.id.eq(id))
                .fetchOne();
    }
}
