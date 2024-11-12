package hyundai.movie_review.movie.dto;

import hyundai.movie_review.actor.dto.ActorInfoListDto;
import hyundai.movie_review.director.dto.DirectorInfoDto;
import hyundai.movie_review.director.dto.DirectorInfoListDto;
import hyundai.movie_review.gallery.entity.dto.GalleryInfoListDto;
import hyundai.movie_review.genre.dto.GenreInfoListDto;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.review.dto.ReviewCountListDto;
import hyundai.movie_review.tag.dto.TagInfoListDto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "영화 디테일 정보 DTO")
public record MovieDetailResponse(

        @Schema(description = "영화의 ID", example = "100")
        long movieId,

        @Schema(description = "영화 정보")
        MovieInfoDto movieInfo,

        @Schema(description = "배우 정보 목록")
        ActorInfoListDto actorInfoList,

        @Schema(description = "감독 정보")
        DirectorInfoListDto directorInfoList,
        @Schema(description = "갤러리 정보 목록")
        GalleryInfoListDto galleryInfoList,

        @Schema(description = "장르 정보 목록")
        GenreInfoListDto genreInfoList,

        @Schema(description = "태그 정보 목록")
        TagInfoListDto tagInfoList,

        @Schema(description = "영화에 대한 리뷰 정보 목록")
        ReviewCountListDto reviewCountInfo
) {

    public static MovieDetailResponse of(Movie movie, ReviewCountListDto reviewCountListDto) {
        return new MovieDetailResponse(
                movie.getId(),
                MovieInfoDto.of(movie),
                ActorInfoListDto.of(movie.getActors()),
                DirectorInfoListDto.of(movie.getDirectors()),
                GalleryInfoListDto.of(movie.getGalleries()),
                GenreInfoListDto.of(movie.getGenres()),
                TagInfoListDto.of(movie.getTags()),
                reviewCountListDto
        );
    }

}
