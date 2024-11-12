package hyundai.movie_review.thear_up.dto;

public record  ThearUpResponse(
        String message
) {
    public static ThearUpResponse of(String message) {
        return new ThearUpResponse(message);
    }
}
