package hyundai.movie_review.badge.constant;

import lombok.Getter;

// 특정 리뷰의 좋아요 또는 댓글 수에 따라 달성되는 배지 조건을 정의
@Getter
public enum BadgeConditionBySpecificReview {

    SINGLE_REVIEW_UP_30(12L, "하나의 리뷰에 UP 30개 달성", 30),
    SINGLE_REVIEW_COMMENT_10(13L, "하나의 리뷰에 댓글 10개 달성", 10);

    private final long badgeId;
    private final String description;
    private final int threshold;

    BadgeConditionBySpecificReview(long badgeId, String description, int threshold) {
        this.badgeId = badgeId;
        this.description = description;
        this.threshold = threshold;
    }
}
