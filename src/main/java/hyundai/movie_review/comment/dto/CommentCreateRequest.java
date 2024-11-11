package hyundai.movie_review.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentCreateRequest(
        @Schema(description = "리뷰 ID", example = "1")
        Long reviewId,

        @Schema(description = "댓글 내용", example = "완전 공감돼요!!")
        String content
) {

}
