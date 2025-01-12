package hyundai.movie_review.tier.constant;

import lombok.Getter;

@Getter
public enum TierLevel {
    LV1(1L, "띠어력 신입", 0L, 99L),
    LV2(2L, "띠어력 초보", 100L, 199L),
    LV3(3L, "띠어력 중수", 200L, 299L),
    LV4(4L, "띠어력 고수", 300L, 499L),
    LV5(5L, "띠어력 초고수", 500L, 699L),
    LV6(6L, "영화팬", 700L, 999L),
    LV7(7L, "방구석 평론가", 1000L, 1499L),
    LV8(8L, "영화광", 1500L, 1999L),
    LV9(9L, "영화 마스터", 2000L, 4999L),
    LV10(10L, "영화의 신", 5000L, Long.MAX_VALUE);

    private final long id;
    private final String name;
    private final long lowerLimit;
    private final long upperLimit;

    TierLevel(long id, String name, long lowerLimit, long upperLimit) {
        this.id = id;
        this.name = name;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    public static TierLevel getTierByScore(long score) {
        for (TierLevel tier : TierLevel.values()) {
            if (score >= tier.getLowerLimit() && score <= tier.getUpperLimit()) {
                return tier;
            }
        }
        return LV10; // 상한선을 초과하는 경우 최상위 티어로 반환
    }
}
