package hyundai.movie_review.comment.dto;


import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CommentGetResponse (
        @Schema(description = "댓글 ID", example = "1")
        Long commentId,

        @Schema(description = "리뷰 ID", example = "1")
        Long reviewId,

        @Schema(description = "작성 멤버 ID", example = "1")
        Long memberId,

        @Schema(description = "작성 멤버 이름", example = "홍길동")
        String name,

        @Schema(description = "작성 멤버 프로필 사진", example = "http://k.kakaocdn.net/...")
        String profileImage,

        @Schema(description = "작성 멤버 티어 이미지 경로", example = "1")
        String tierImage,

        @Schema(description = "작성 멤버 대표뱃지 이미지 경로", example = "1")
        String badgeImage,

        @Schema(description = "댓글 내용", example = "완전 공감해요!!")
        String content,

        @Schema(description = "댓글 생성날짜", example = "2024-11-11T12:37:29.3846313")
        LocalDateTime createdAt
){
        public static CommentGetResponse of(Member member, Long reviewId, Comment comment){
                return new CommentGetResponse(
                        comment.getId(),
                        reviewId,
                        member.getId(),
                        member.getName(),
                        member.getProfileImage(),
                        member.getTier().getImage(),
                        member.getBadge().getImage(),
                        comment.getContent(),
                        comment.getCreatedAt()
                );
        }
}
