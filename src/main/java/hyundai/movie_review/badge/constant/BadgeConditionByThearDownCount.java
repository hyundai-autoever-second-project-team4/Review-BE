package hyundai.movie_review.badge.constant;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum BadgeConditionByThearDownCount {
    DOWN_10(15L, "리뷰 DOWN 총 10개 달성", 10);

    private final long badgeId;
    private final String description;
    private final int threshold;

    BadgeConditionByThearDownCount(long badgeId, String description, int threshold) {
        this.badgeId = badgeId;
        this.description = description;
        this.threshold = threshold;
    }

    public static Optional<BadgeConditionByThearDownCount> getHighestConditionByThearDownCount(
            long thearDownCount) {
        return Arrays.stream(values())
                .filter(condition -> thearDownCount == condition.getThreshold())
                .findFirst();
    }
}
