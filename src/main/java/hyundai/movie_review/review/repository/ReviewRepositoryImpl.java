package hyundai.movie_review.review.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.comment.entity.QComment;
import hyundai.movie_review.member.entity.QMember;
import hyundai.movie_review.review.dto.ReviewInfoListDto;
import hyundai.movie_review.review.dto.ReviewCountDto;
import hyundai.movie_review.review.dto.ReviewCountListDto;
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
    public ReviewCountListDto getReviewCountsByMovieId(Long movieId) {
        QReview review = QReview.review;

        // 1) 해당 영화의 모든 리뷰를 가져오기
        List<Review> reviews = queryFactory
                .selectFrom(review)
                .where(review.movie.id.eq(movieId))
                .fetch();

        // 2) starRate별로 그룹화하여 카운팅
        Map<Double, Long> reviewCountMap = reviews.stream()
                .collect(Collectors.groupingBy(
                        Review::getStarRate,
                        Collectors.counting()
                ));

        // 3) ReviewCountDto 리스트로 변환
        List<ReviewCountDto> reviewCounts = reviewCountMap.entrySet().stream()
                .map(entry -> ReviewCountDto.of(entry.getKey(), entry.getValue()))
                .toList();

        // 4) 총 리뷰 개수와 평점 평균을 계산합니다.
        long totalReviewCount = reviews.size();
        double averageStarRate = reviews.stream()
                .mapToDouble(Review::getStarRate)
                .average()
                .orElse(0.0);

        return ReviewCountListDto.of(totalReviewCount, averageStarRate, reviewCounts);
    }

}
