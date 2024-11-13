package hyundai.movie_review.movie.dto;

import java.util.List;

public record MovieListResponse(
        List<MovieWithRatingInfoDto> movies
) {

    public static MovieListResponse of(List<MovieWithRatingInfoDto> movieInfoDtos) {
        return new MovieListResponse(movieInfoDtos);
    }

}
