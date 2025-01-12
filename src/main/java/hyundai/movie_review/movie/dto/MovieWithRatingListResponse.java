package hyundai.movie_review.movie.dto;

import java.util.List;

public record MovieWithRatingListResponse(
        List<MovieWithRatingInfoDto> movies
) {

    public static MovieWithRatingListResponse of(List<MovieWithRatingInfoDto> movieInfoDtos) {
        return new MovieWithRatingListResponse(movieInfoDtos);
    }

}
