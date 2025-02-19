package hyundai.movie_review.movie.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.genre.entity.QGenre;
import hyundai.movie_review.movie.dto.MovieWithRatingInfoDto;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.entity.QMovie;
import hyundai.movie_review.movie_genre.entity.QMovieGenre;
import hyundai.movie_review.review.entity.QReview;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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
                .join(review).on(review.movie.id.eq(movie.id))
                .where(review.createdAt.between(startDate, endDate)
                        .and(review.deleted.isFalse()))
                .groupBy(movie.id)
                .orderBy(review.starRate.avg().desc(), review.count().desc()) // 평균 별점 -> 리뷰 개수 순 정렬
                .limit(10)
                .fetch();
    }

    @Override
    public List<MovieWithRatingInfoDto> findMoviesByMostReviewsThisWeek() {
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
                .join(review).on(review.movie.id.eq(movie.id))
                .where(review.createdAt.between(startDate, endDate)
                        .and(review.deleted.isFalse()))
                .groupBy(movie.id)
                .orderBy(review.count().desc(), review.starRate.avg().desc()) // 리뷰 수 -> 평균 별점 순 정렬
                .limit(10)
                .fetch();
    }

    @Override
    public List<MovieWithRatingInfoDto> findRecommendedMoviesForMemberByGenreId(long genreId, long memberId) {
        QMovie movie = QMovie.movie;
        QGenre genre = QGenre.genre;
        QMovieGenre movieGenre = QMovieGenre.movieGenre;
        QReview review = QReview.review;

        // 회원이 작성한 리뷰에 해당하는 Movie ID 조회
        List<Long> reviewedMovieIds = queryFactory
                .select(review.movie.id)
                .from(review)
                .where(review.member.id.eq(memberId)) // 작성한 리뷰의 movieId만 가져옴
                .fetch();

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
                        movie.totalReviewCount, // totalReviewCount 필드 매핑
                        movie.totalStarRate     // totalStarRate 필드 매핑
                ))
                .from(movie)
                .join(movieGenre).on(movieGenre.movie.eq(movie))
                .join(movieGenre.genre, genre)
                .where(
                        genre.id.eq(genreId),
                        movie.id.notIn(reviewedMovieIds) // 작성한 리뷰의 movieId 제외
                )
                .orderBy(
                        movie.totalStarRate.desc(),
                        movie.totalReviewCount.desc()
                )
                .limit(10)
                .fetch();
        }

    @Override
    public List<MovieWithRatingInfoDto> findHonorBoardMovies() {
        QMovie movie = QMovie.movie;
        QReview review = QReview.review;

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
                .join(review).on(review.movie.id.eq(movie.id))
                .groupBy(movie.id)
                .orderBy(
                        review.count().desc(),       // 리뷰 수가 많은 순
                        review.starRate.avg().desc() // 별점이 높은 순
                )
                .limit(10)
                .fetch();
    }

    @Override
    public Page<Movie> findMoviesByGenreName(String genreName, Pageable pageable) {
        QMovie movie = QMovie.movie;
        QMovieGenre movieGenre = QMovieGenre.movieGenre;
        QGenre genre = QGenre.genre;

        List<Movie> movies = queryFactory
                .select(movie)
                .from(movie)
                .join(movie.genres, movieGenre)
                .join(movieGenre.genre, genre)
                .where(genre.name.like("%" + genreName.toLowerCase() + "%"))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 계산
        long total = Objects.requireNonNullElse(
                queryFactory
                        .select(movie.count())
                        .from(movie)
                        .join(movie.genres, movieGenre)
                        .join(movieGenre.genre, genre)
                        .where(Expressions.stringTemplate("lower({0})", genre.name)
                                .like("%" + genreName.toLowerCase() + "%"))
                        .fetchOne(),
                0L
        );

        return new PageImpl<>(movies, pageable, total);
    }

}
