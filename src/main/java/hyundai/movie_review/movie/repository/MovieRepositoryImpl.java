package hyundai.movie_review.movie.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundai.movie_review.actor.dto.ActorInfoDto;
import hyundai.movie_review.actor.dto.ActorInfoListDto;
import hyundai.movie_review.actor.entity.QActor;
import hyundai.movie_review.director.dto.DirectorInfoDto;
import hyundai.movie_review.director.dto.DirectorInfoListDto;
import hyundai.movie_review.director.entity.QDirector;
import hyundai.movie_review.gallery.entity.QGallery;
import hyundai.movie_review.gallery.entity.dto.GalleryInfoDto;
import hyundai.movie_review.gallery.entity.dto.GalleryInfoListDto;
import hyundai.movie_review.genre.dto.GenreInfoDto;
import hyundai.movie_review.genre.dto.GenreInfoListDto;
import hyundai.movie_review.genre.entity.QGenre;
import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.dto.MovieInfoDto;
import hyundai.movie_review.movie.entity.QMovie;
import hyundai.movie_review.movie_genre.entity.QMovieGenre;
import hyundai.movie_review.movie_tag.entity.QMovieTag;
import hyundai.movie_review.tag.dto.TagInfoDto;
import hyundai.movie_review.tag.dto.TagInfoListDto;
import hyundai.movie_review.tag.entity.QTag;
import java.util.List;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class MovieRepositoryImpl implements MovieRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MovieRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public MovieDetailResponse findMovieDetailById(Long movieId) {
        QMovie movie = QMovie.movie;
        QDirector director = QDirector.director;
        QActor actor = QActor.actor;
        QGallery gallery = QGallery.gallery;
        QGenre genre = QGenre.genre;
        QTag tag = QTag.tag;
        QMovieGenre movieGenre = QMovieGenre.movieGenre;
        QMovieTag movieTag = QMovieTag.movieTag;

        // MovieInfoDto 매핑
        MovieInfoDto movieInfoDto = queryFactory
                .select(Projections.constructor(MovieInfoDto.class,
                        movie.title,
                        movie.overview,
                        movie.posterPath,
                        movie.backdropPath,
                        movie.adult,
                        movie.releaseDate,
                        movie.runtime,
                        movie.originCountry,
                        movie.totalReviewCount,
                        movie.totalStarRate
                ))
                .from(movie)
                .where(movie.movieId.eq(movieId))
                .fetchOne();

        // DirectorInfoDto 매핑

        List<DirectorInfoDto> directorInfoDtos = queryFactory
                .select(Projections.constructor(DirectorInfoDto.class,
                        director.id,
                        director.movieId,
                        director.name,
                        director.profilePath))
                .from(director)
                .where(director.movieId.eq(movieId))
                .fetch();
        DirectorInfoListDto directorInfoListDto = new DirectorInfoListDto(directorInfoDtos);

        // ActorInfoListDto 매핑
        List<ActorInfoDto> actors = queryFactory
                .select(Projections.constructor(ActorInfoDto.class,
                        actor.id,
                        actor.movieId,
                        actor.name,
                        actor.characterName,
                        actor.profilePath))
                .from(actor)
                .where(actor.movieId.eq(movieId))
                .fetch();

        ActorInfoListDto actorInfoListDto = new ActorInfoListDto(actors);

        // GalleryInfoListDto 매핑
        List<GalleryInfoDto> galleries = queryFactory
                .select(Projections.constructor(GalleryInfoDto.class,
                        gallery.movieId,
                        gallery.galleryPath))
                .from(gallery)
                .where(gallery.movieId.eq(movieId))
                .fetch();
        GalleryInfoListDto galleryInfoListDto = new GalleryInfoListDto(galleries);

        // GenreInfoListDto 매핑
        List<GenreInfoDto> genres = queryFactory
                .select(Projections.constructor(GenreInfoDto.class,
                        genre.id,
                        genre.name))
                .from(movieGenre)
                .leftJoin(genre).on(movieGenre.genreId.eq(genre.id))
                .where(movieGenre.movieId.eq(movieId))
                .fetch();
        GenreInfoListDto genreInfoListDto = new GenreInfoListDto(genres);

        // TagInfoListDto 매핑
        List<TagInfoDto> tags = queryFactory
                .select(Projections.constructor(TagInfoDto.class,
                        tag.content,  // 실제 필드 이름을 확인하여 적절히 수정
                        tag.img)) // 실제 필드 이름을 확인하여 적절히 수정
                .from(movieTag)
                .leftJoin(tag).on(movieTag.tagId.eq(tag.id))
                .where(movieTag.movieId.eq(movieId))
                .fetch();
        TagInfoListDto tagInfoListDto = new TagInfoListDto(tags);

        // 최종 MovieInfoDto 매핑
        MovieDetailResponse movieDetailResponse = new MovieDetailResponse(
                movieId,
                movieInfoDto,
                directorInfoListDto,
                actorInfoListDto,
                galleryInfoListDto,
                genreInfoListDto,
                tagInfoListDto
        );

        return movieDetailResponse;
    }

}
