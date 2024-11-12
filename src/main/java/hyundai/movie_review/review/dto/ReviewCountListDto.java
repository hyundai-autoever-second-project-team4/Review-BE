package hyundai.movie_review.review.dto;

import java.util.List;

public record ReviewCountListDto(
        List<ReviewCountDto> reviewCounts
) {

    public static ReviewCountListDto of(List<ReviewCountDto> reviewCountDtos) {
        return new ReviewCountListDto(reviewCountDtos);
    }

}
