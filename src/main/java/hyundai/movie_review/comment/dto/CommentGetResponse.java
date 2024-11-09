package hyundai.movie_review.comment.dto;


import java.time.LocalDateTime;

public record CommentGetResponse (
        Long id,
        Long memberId,
        Long reviewId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){
}
