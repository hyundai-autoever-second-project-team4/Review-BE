package hyundai.movie_review.review.dto;

import org.springframework.data.domain.Page;

public record ReviewInfoPageDto(
        Page<ReviewInfoDto> reviewInfos
) {
    public static ReviewInfoPageDto of(Page<ReviewInfoDto> reviewInfos){ return new ReviewInfoPageDto(reviewInfos); }
}
