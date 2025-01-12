package hyundai.movie_review.alarm.dto;

public record AlarmReadResponse(
        String message
) {

    public static AlarmReadResponse of(String message) {
        return new AlarmReadResponse(message);
    }

}
