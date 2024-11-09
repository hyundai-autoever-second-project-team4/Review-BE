package hyundai.movie_review.comment.dto;

import java.time.LocalDateTime;

public record CommentUpdateResponse(
        Long reviewId,
        String content,
        LocalDateTime updatedAt
) {
}
