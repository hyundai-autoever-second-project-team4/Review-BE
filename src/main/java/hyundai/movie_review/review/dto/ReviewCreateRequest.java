package hyundai.movie_review.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "리뷰 생성 요청 데이터")
public record ReviewCreateRequest(

        @Schema(description = "영화 ID", example = "1")
        Long movieId,

        @Schema(description = "별점", example = "4.5")
        Double starRate,

        @Schema(description = "리뷰 내용", example = "영화가 매우 훌륭했습니다.")
        String content,

        @Schema(description = "스포일러 포함 여부", example = "false")
        Boolean spoiler

) {
}
