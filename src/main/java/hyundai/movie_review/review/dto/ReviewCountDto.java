package hyundai.movie_review.review.dto;

public record ReviewCountDto(
        double starRate,
        long count
) {

    public static ReviewCountDto of(double starRate, long count) {
        return new ReviewCountDto(starRate, count);
    }

}
