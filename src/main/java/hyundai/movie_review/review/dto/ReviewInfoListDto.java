package hyundai.movie_review.review.dto;

import java.util.List;

public record ReviewInfoListDto(
        List<ReviewInfoDto> reviewInfos
) {

    public static ReviewInfoListDto of(List<ReviewInfoDto> reviewInfos) {
        return new ReviewInfoListDto(reviewInfos);
    }
}
