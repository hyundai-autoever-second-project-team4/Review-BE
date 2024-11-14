package hyundai.movie_review.badge.constant;

import lombok.Getter;

// 리뷰 수에 따라 달성되는 배지 조건을 정의
@Getter
public enum BadgeConditionByTotalReviewCount {

    REVIEW_3(2L, "리뷰 3개 달성", 3),
    REVIEW_20(3L, "리뷰 20개 달성", 20),
    REVIEW_100(4L, "리뷰 100개 달성", 100),
    REVIEW_1000(5L, "리뷰 1000개 달성", 1000);

    private final long badgeId;
    private final String description;
    private final int threshold;

    BadgeConditionByTotalReviewCount(long badgeId, String description, int threshold) {
        this.badgeId = badgeId;
        this.description = description;
        this.threshold = threshold;
    }
}
