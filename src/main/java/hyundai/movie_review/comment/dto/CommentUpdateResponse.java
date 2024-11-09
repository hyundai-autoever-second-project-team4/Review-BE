package hyundai.movie_review.comment.dto;

import java.time.LocalDateTime;

public record CommentUpdateResponse(
        Long commentId,
        String content,
        LocalDateTime updatedAt
) {
}
