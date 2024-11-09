package hyundai.movie_review.comment.dto;

import java.time.LocalDateTime;

public record CommentCreateResponse(
        Long memberId,
        Long reviewId,
        String content,
        LocalDateTime createdAt
) {
}
