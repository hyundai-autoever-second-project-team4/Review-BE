package hyundai.movie_review.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewInfoListDto(
    @Schema(description = "멤버 id")
    Long memberId,

    @Schema(description = "멤버 이름")
    String memberName,

    @Schema(description = "멤버 프로필 사진 경로")
    String memberProfileImg,

    @Schema(description = "멤버 티어 이미지 경로")
    String memberTierImg,

    @Schema(description = "리뷰 id")
    Long reviewId,

    @Schema(description = "리뷰 별점")
    Double starRate,

    @Schema(description = "리뷰 내용")
    String content,

    @Schema(description = "스포일러 여부")
    Boolean spoiler,

    @Schema(description = "띠어럽/좋아요 개수")
    Long ThearUpCount,

    @Schema(description = "띠어다운/싫어요 개수")
    Long ThearDownCount,

    @Schema(description = "리뷰에 대한 댓글 개수")
    Long commentCount,

    @Schema(description = "내가 리뷰에 띠어럽/좋아요 체크 여부")
    Boolean isThearUp,

    @Schema(description = "내가 리뷰에 띠어다운/싫어요 체크 여부")
    Boolean isThearDown
){
    public static ReviewInfoListDto of(
            Long memberId,
            String memberName,
            String memberProfileImg,
            String memberTierImg,
            Long reviewId,
            Double starRate,
            String content,
            Boolean spoiler,
            Long ThearUpCount,
            Long ThearDownCount,
            Long commentCount,
            Boolean isThearUp,
            Boolean isThearDown
    ){
        return new ReviewInfoListDto(
                memberId,
                memberName,
                memberProfileImg,
                memberTierImg,
                reviewId,
                starRate,
                content,
                spoiler,
                ThearUpCount,
                ThearDownCount,
                commentCount,
                isThearUp,
                isThearDown
        );
    }
}
