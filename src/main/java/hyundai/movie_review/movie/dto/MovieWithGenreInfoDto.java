package hyundai.movie_review.movie.dto;

import hyundai.movie_review.genre.dto.GenreInfoDto;
import hyundai.movie_review.genre.dto.GenreInfoListDto;
import hyundai.movie_review.movie.entity.Movie;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record MovieWithGenreInfoDto(
        long movieId,
        String title,
        String posterPath,
        String originCountry,
        boolean adult,
        long runtime,
        LocalDateTime releaseDate,
        List<GenreInfoDto> genres
) {

    public static MovieWithGenreInfoDto of(Movie movie) {
        return new MovieWithGenreInfoDto(
                movie.getId(),
                movie.getTitle(),
                movie.getPosterPath(),
                movie.getOriginCountry(),
                movie.getAdult(),
                movie.getRuntime(),
                movie.getReleaseDate(),
                movie.getGenres().stream()
                        .map(GenreInfoDto::of)
                        .collect(Collectors.toList())
        );
    }

}
