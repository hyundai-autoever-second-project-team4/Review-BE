package hyundai.movie_review.review.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.comment.entity.QComment;
import hyundai.movie_review.member.entity.QMember;
import hyundai.movie_review.review.dto.ReviewByMovieIdDto;
import hyundai.movie_review.review.dto.ReviewCountDto;
import hyundai.movie_review.review.entity.QReview;
import hyundai.movie_review.review.entity.Review;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import hyundai.movie_review.thear_down.entity.QThearDown;
import hyundai.movie_review.thear_up.entity.QThearUp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ReviewCountDto> getReviewCountsByMovieId(Long movieId) {
        QReview review = QReview.review;

        // 1) 해당 영화의 모든 리뷰를 가져옵니다.
        List<Review> reviews = queryFactory
                .selectFrom(review)
                .where(review.movieId.eq(movieId))
                .fetch();

        // 2) starRate별로 그룹화하여 카운팅합니다.
        Map<Double, Long> reviewCountMap = reviews.stream()
                .collect(Collectors.groupingBy(
                        Review::getStarRate,
                        Collectors.counting()
                ));

        // 3) ReviewCountDto 리스트로 변환합니다.
        return reviewCountMap.entrySet().stream()
                .map(entry -> ReviewCountDto.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReviewByMovieIdDto> getReviewInfosByMovieId(Long movieId, Pageable pageable) {
        QReview review = QReview.review;
        QMember member = QMember.member;
        QThearUp thearUp = QThearUp.thearUp;
        QThearDown thearDown = QThearDown.thearDown;
        QComment comment = QComment.comment;

        // 1) 해당 영화의 리뷰의 정보와 각 리뷰를 작성한 멤버 정보를 가져옵니다.
        List<ReviewByMovieIdDto> reviews = queryFactory
                .select(Projections.constructor(ReviewByMovieIdDto.class,
                        member.id,
                        member.name,
                        member.profileImage,
                        member.tier.image,
                        review.id,
                        review.starRate,
                        review.content,
                        review.spoiler,
                        thearUp.count(),
                        thearDown.count(),
                        comment.count()
                        ))
                .from(review)
                .leftJoin(member).on(review.memberId.eq(member.id))
                .leftJoin(comment).on(review.id.eq(comment.reviewId.id))
                .leftJoin(thearUp).on(review.id.eq(thearUp.reviewId.id))
                .leftJoin(thearDown).on(review.id.eq(thearDown.reviewId.id))
                .where(review.movieId.eq(movieId))
                .groupBy(review.id, member.id)
                .orderBy(thearUp.count().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2) 전체 개수 조회
        Long reviewCount = queryFactory.select(review).from(review)
                .where(review.movieId.eq(movieId))
                .fetchCount();

        log.info("실행결과 : {} , reviews : {}", reviews, reviewCount);

        return new PageImpl<>(reviews, pageable, reviewCount);
    }
}
