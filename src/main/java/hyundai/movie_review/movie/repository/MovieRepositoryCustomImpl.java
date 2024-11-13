package hyundai.movie_review.movie.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.movie.entity.Movie;
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
    public List<Movie> findMoviesByHighestRatingThisWeek() {
        QMovie movie = QMovie.movie;
        QReview review = QReview.review;

        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();

        return queryFactory
                .select(movie)
                .from(movie)
                .join(review).on(review.movieId.eq(movie.id))
                .where(review.createdAt.between(startDate, endDate)
                        .and(review.deleted.isFalse()))
                .groupBy(movie.id)
                .orderBy(review.starRate.avg().desc())
                .limit(10)  // 상위 10개 영화만 가져오기
                .fetch();
    }
}
