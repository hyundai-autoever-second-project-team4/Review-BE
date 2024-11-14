package hyundai.movie_review.badge.constant;

import lombok.Getter;

// 특정 장르의 리뷰 수에 따라 달성되는 배지 조건을 정의
@Getter
public enum BadgeConditionByGenreCount {

    ROMANCE_10(9L, "로맨스 장르 영화 리뷰 10개 달성", "Romance", 10),
    ACTION_10(10L, "액션 장르 영화 리뷰 10개 달성", "Action", 10),
    ANIMATION_10(11L, "애니메이션 장르 영화 리뷰 10개 달성", "Animation", 10);

    private final long badgeId;
    private final String description;
    private final String genre;
    private final int threshold;

    BadgeConditionByGenreCount(long badgeId, String description, String genre, int threshold) {
        this.badgeId = badgeId;
        this.description = description;
        this.genre = genre;
        this.threshold = threshold;
    }
}
