package hyundai.movie_review.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewThearUpDownCheckedDto(
        @Schema(description = "ThearUp 여부")
        boolean isThearUp,

        @Schema(description = "ThearDown 여부")
        boolean isThearDown
) {
    public static ReviewThearUpDownCheckedDto of(
            boolean isThearUp,
            boolean isThearDown
    ){
        return new ReviewThearUpDownCheckedDto(
                isThearUp,
                isThearDown
        );
    }

}
