package hyundai.movie_review.comment.dto;

public record CommentCreateRequest(
        Long reviewId,
        String content
) {

}
