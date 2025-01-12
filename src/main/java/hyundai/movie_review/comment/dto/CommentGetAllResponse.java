package hyundai.movie_review.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

public record CommentGetAllResponse (
        @Schema(description = "전체 리뷰 개수", example = "15")
        Long totalComment,

        @Schema(description = "리뷰 리스트")
        Page<CommentGetResponse> commentList
){
}

