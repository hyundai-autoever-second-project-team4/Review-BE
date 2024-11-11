package hyundai.movie_review.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CommentUpdateResponse(
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,

        @Schema(description = "댓글 내용", example = "댓글 수정할래요")
        String content,

        @Schema(description = "댓글 생성날짜", example = "2024-11-11T12:37:29.3846313")
        LocalDateTime updatedAt
) {
}
