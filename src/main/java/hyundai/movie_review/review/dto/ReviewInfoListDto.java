package hyundai.movie_review.review.dto;

import java.util.List;

public record ReviewInfoListDto(
        List<ReviewInfoDto> reviewInfos
) {
}
