package hyundai.movie_review.comment.dto;

public record CommentUpdateRequest(
        Long commentId,
        String content
) {
}
