package hyundai.movie_review.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CommentCreateResponse(
        @Schema(description = "멤버 ID", example = "1")
        Long memberId,

        @Schema(description = "리뷰 ID", example = "1")
        Long reviewId,

        @Schema(description = "댓글 내용", example = "완전 공감해요!!")
        String content,

        @Schema(description = "댓글 생성날짜", example = "2024-11-11T12:37:29.3846313")
        LocalDateTime createdAt
) {
}
