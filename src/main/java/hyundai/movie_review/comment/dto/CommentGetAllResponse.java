package hyundai.movie_review.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record CommentGetAllResponse (
        @Schema(description = "전체 리뷰 개수", example = "15")
        Long totalComment,

        @Schema(description = "리뷰 리스트")
        List<CommentDto> commentList
){
    public record CommentDto(
            @Schema(description = "댓글 ID", example = "1")
            Long commentId,

            @Schema(description = "리뷰 ID", example = "1")
            Long reviewId,

            @Schema(description = "멤버 이름", example = "홍길동")
            String name,

            @Schema(description = "멤버 프로필 사진", example = "http://k.kakaocdn.net/...")
            String profileImage,

            @Schema(description = "티어 ID", example = "1")
            Long tierImage,

            @Schema(description = "대표뱃지 ID", example = "1")
            Long badgeImage,

            @Schema(description = "댓글 내용", example = "완전 공감해요!!")
            String content,

            @Schema(description = "댓글 생성날짜", example = "2024-11-11T12:37:29.3846313")
            LocalDateTime createdAt
    ){}
}

