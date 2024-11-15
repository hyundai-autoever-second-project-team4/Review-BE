package hyundai.movie_review.review.dto;

import hyundai.movie_review.comment.dto.CommentGetAllResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "리뷰 상세 모달 정보")
public record ReviewDetailInfoDto(
        @Schema(description = "리뷰 정보")
        ReviewInfoDto reviewInfo,

        @Schema(description = "댓글 정보")
        CommentGetAllResponse commentInfo

) {
    public static ReviewDetailInfoDto of(
            ReviewInfoDto reviewInfo,
            CommentGetAllResponse commentInfo
    ){
        return new ReviewDetailInfoDto(reviewInfo, commentInfo);
    }
}
