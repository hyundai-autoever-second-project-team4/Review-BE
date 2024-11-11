package hyundai.movie_review.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.badge.entity.QBadge;
import hyundai.movie_review.member.dto.MemberBadgeAndTierDto;
import hyundai.movie_review.member.entity.QMember;
import hyundai.movie_review.tier.entity.QTier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public MemberBadgeAndTierDto getTierAndBadgeImgByMemberId(Long id) {
        return jpaQueryFactory.select(Projections.constructor(MemberBadgeAndTierDto.class,
                QBadge.badge.image,
                QTier.tier.image))
                .from(QMember.member)
                .join(QBadge.badge).on(QMember.member.badgeId.eq(QBadge.badge.id))
                .join(QTier.tier).on(QMember.member.tierId.eq(QTier.tier.id))
                .where(QMember.member.id.eq(id))
                .fetchOne();
    }
}
