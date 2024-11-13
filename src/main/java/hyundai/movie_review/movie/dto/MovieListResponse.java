package hyundai.movie_review.movie.dto;

import java.util.List;

public record MovieListResponse(
        List<MovieInfoDto> movies
) {

    public static MovieListResponse of(List<MovieInfoDto> movieInfoDtos) {
        return new MovieListResponse(movieInfoDtos);
    }

}
