package hyundai.movie_review.movie.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.movie.dto.MovieWithRatingInfoDto;
import hyundai.movie_review.movie.entity.QMovie;
import hyundai.movie_review.review.entity.QReview;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieRepositoryCustomImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MovieWithRatingInfoDto> findMoviesByHighestRatingThisWeek() {
        QMovie movie = QMovie.movie;
        QReview review = QReview.review;

        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        return queryFactory
                .select(Projections.constructor(MovieWithRatingInfoDto.class,
                        movie.id,
                        movie.title,
                        movie.overview,
                        movie.posterPath,
                        movie.backdropPath,
                        movie.adult,
                        movie.releaseDate,
                        movie.runtime,
                        movie.originCountry,
                        review.count().as("totalReviewCount"),
                        review.starRate.avg().as("averageStarRate")
                ))
                .from(movie)
                .join(review).on(review.movieId.eq(movie.id))
                .where(review.createdAt.between(startDate, endDate)
                        .and(review.deleted.isFalse()))
                .groupBy(movie.id)
                .orderBy(review.starRate.avg().desc(), review.count().desc()) // 평균 별점 -> 리뷰 개수 순 정렬
                .limit(10)
                .fetch();
    }
}
