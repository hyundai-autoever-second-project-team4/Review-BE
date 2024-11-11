package hyundai.movie_review.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommentUpdateRequest(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,

        @Schema(description = "댓글 내용", example = "댓글 수정할래요")
        String content
) {
}
