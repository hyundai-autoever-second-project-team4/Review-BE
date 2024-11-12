package hyundai.movie_review.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record CommentGetAllResponse (
        @Schema(description = "전체 리뷰 개수", example = "15")
        Long totalComment,

        @Schema(description = "리뷰 리스트")
        List<CommentGetResponse> commentList
){
}

