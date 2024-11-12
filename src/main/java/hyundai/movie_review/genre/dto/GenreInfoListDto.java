package hyundai.movie_review.genre.dto;

import hyundai.movie_review.movie_genre.entity.MovieGenre;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "영화에 대한 장르 리스트 DTO")
public record GenreInfoListDto(
        @Schema(description = "영화에 대한 장르 리스트")
        List<GenreInfoDto> genres
) {

    public static GenreInfoListDto of(List<MovieGenre> genres) {
        List<GenreInfoDto> genreInfoDtos = genres.stream()
                .map(GenreInfoDto::of)
                .toList();

        return new GenreInfoListDto(genreInfoDtos);
    }
}
