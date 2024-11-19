package hyundai.movie_review.member.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.comment.entity.QComment;
import hyundai.movie_review.member.dto.MemberBadgeAndTierDto;

import static hyundai.movie_review.badge.entity.QBadge.badge;
import static hyundai.movie_review.member.entity.QMember.member;
import static hyundai.movie_review.member_badge.entity.QMemberBadge.memberBadge;
import static hyundai.movie_review.tier.entity.QTier.tier;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.entity.QMember;
import hyundai.movie_review.review.entity.QReview;
import hyundai.movie_review.thear_up.entity.QThearUp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    // 멤버 티어와 대표 뱃지 이미지 경로 dto
    @Override
    public MemberBadgeAndTierDto getTierAndBadgeImgByMemberId(Long id) {
        return jpaQueryFactory.select(Projections.constructor(MemberBadgeAndTierDto.class,
                        badge.image,
                        tier.image))
                .from(member)
                .join(badge).on(member.badge.eq(badge))
                .join(member.tier, tier)
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public Page<Member> findAllOrderByReviewCount(Pageable pageable) {
        QMember qMember = QMember.member;
        QReview qReview = QReview.review;

        // 리뷰 개수로 정렬
        JPQLQuery<Member> query = jpaQueryFactory
                .select(qMember)
                .from(qMember)
                .leftJoin(qMember.reviews, qReview)
                .groupBy(qMember.id)
                .orderBy(
                        Expressions.numberTemplate(Long.class, "count({0})", qReview.id).desc(),
                        qMember.totalScore.desc()   // 리뷰가 같을 경우 점수가 높은 순으로 정렬
                );

        return getPagedResult(query, pageable);
    }

    @Override
    public Page<Member> findAllOrderByUpCount(Pageable pageable) {
        QMember qMember = QMember.member;
        QThearUp qThearUp = QThearUp.thearUp;

        // 받은 'Up' 개수로 정렬
        JPQLQuery<Member> query = jpaQueryFactory
                .select(qMember)
                .from(qMember)
                .leftJoin(qMember.thearUps, qThearUp)
                .groupBy(qMember.id)
                .orderBy(
                        Expressions.numberTemplate(Long.class, "count({0})", qThearUp.id).desc(),
                        qMember.totalScore.desc()
                );
        return getPagedResult(query, pageable);
    }

    @Override
    public Page<Member> findAllOrderByCommentCount(Pageable pageable) {
        QMember qMember = QMember.member;
        QComment qComment = QComment.comment;

        // 댓글 개수로 정렬
        JPQLQuery<Member> query = jpaQueryFactory
                .select(qMember)
                .from(qMember)
                .leftJoin(qMember.comments, qComment)
                .groupBy(qMember.id)
                .orderBy(
                        Expressions.numberTemplate(Long.class, "count({0})", qComment.id).desc(),
                        qMember.totalScore.desc()
                );
        return getPagedResult(query, pageable);
    }

    // 페이징 처리하는 공통 메서드
    private Page<Member> getPagedResult(JPQLQuery<Member> query, Pageable pageable) {
        long total = query.fetchCount();
        List<Member> members = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(members, pageable, total);
    }
}
