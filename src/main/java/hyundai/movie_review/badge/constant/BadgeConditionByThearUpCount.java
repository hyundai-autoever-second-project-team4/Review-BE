package hyundai.movie_review.badge.constant;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

// 전체 리뷰 띠어럽 수에 따라 달성되는 배지 조건을 정의
@Getter
public enum BadgeConditionByThearUpCount {

    UP_5(6L, "리뷰 UP 총 5개 달성", 5),
    UP_200(7L, "리뷰 UP 총 200개 달성", 200),
    UP_1000(8L, "리뷰 UP 총 1000개 달성", 1000);

    private final long badgeId;
    private final String description;
    private final int threshold;

    BadgeConditionByThearUpCount(long badgeId, String description, int threshold) {
        this.badgeId = badgeId;
        this.description = description;
        this.threshold = threshold;
    }

    // 주어진 UP count를 만족하는 가장 높은 배지 조건을 반환
    public static Optional<BadgeConditionByThearUpCount> getHighestConditionByThearUpCount(
            long thearUpCount) {
        return Arrays.stream(values())
                .filter(condition -> thearUpCount == condition.getThreshold())
                .findFirst();
    }
}
