package hyundai.movie_review.badge.constant;

import java.util.Optional;
import lombok.Getter;

@Getter
public enum BadgeConditionByBadgeCount {
    TOTAL_BADGES_7(14L, "뱃지 7개 이상 달성", 7);

    private final long badgeId;
    private final String description;
    private final int threshold;

    BadgeConditionByBadgeCount(long badgeId, String description, int threshold) {
        this.badgeId = badgeId;
        this.description = description;
        this.threshold = threshold;
    }

    public static Optional<BadgeConditionByBadgeCount> getBadgeByBadgeCount(
            long badgeCount) {
        for (BadgeConditionByBadgeCount badgeCondition : BadgeConditionByBadgeCount.values()) {
            if (badgeCount == badgeCondition.getThreshold()) {
                return Optional.of(badgeCondition);
            }
        }
        return Optional.empty();
    }
}
