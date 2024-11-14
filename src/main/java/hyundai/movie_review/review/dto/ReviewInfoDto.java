package hyundai.movie_review.review.dto;

import hyundai.movie_review.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;

public record ReviewInfoDto(
        @Schema(description = "멤버 id")
        Long memberId,

        @Schema(description = "멤버 이름")
        String memberName,

        @Schema(description = "멤버 프로필 사진 경로")
        String memberProfileImg,

        @Schema(description = "멤버 티어 이미지 경로")
        String memberTierImg,
        @Schema(description = "리뷰에 해당하는 영화 id")
        Long movieId,
        @Schema(description = "리뷰에 해당하는 영화 제목")
        String movieTitle,

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
) {

    public static ReviewInfoDto of(
            Review review,
            Boolean isThearUp,
            Boolean isThearDown
    ) {
        return new ReviewInfoDto(
                review.getMember().getId(),
                review.getMember().getName(),
                review.getMember().getProfileImage(),
                review.getMember().getTier().getImage(),
                review.getMovie().getId(),
                review.getMovie().getTitle(),
                review.getId(),
                review.getStarRate(),
                review.getContent(),
                review.getSpoiler(),
                review.getThearUps(),
                review.getThearDowns(),
                review.getCommentCounts(),
                isThearUp,
                isThearDown
        );
    }
}
