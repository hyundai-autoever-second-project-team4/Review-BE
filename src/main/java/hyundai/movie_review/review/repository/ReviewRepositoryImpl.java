package hyundai.movie_review.review.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.review.dto.ReviewCountDto;
import hyundai.movie_review.review.entity.QReview;
import hyundai.movie_review.review.entity.Review;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
