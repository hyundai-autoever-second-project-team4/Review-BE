package hyundai.movie_review.review.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record ReviewByMovieIdListDto(
    Page<ReviewByMovieIdDto> reviewListByMovieId,
    List<ReviewThearUpDownCheckedDto> reviewThearUpDown
) {
    public static ReviewByMovieIdListDto of(
            Page<ReviewByMovieIdDto> reviewByMovieIdDtos,
            List<ReviewThearUpDownCheckedDto> reviewThearUpDown
            ){
        return new ReviewByMovieIdListDto(
                reviewByMovieIdDtos,
                reviewThearUpDown
                );
    }
}
