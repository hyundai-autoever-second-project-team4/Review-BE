package hyundai.movie_review.alarm.dto;

import hyundai.movie_review.alarm.entity.Alarm;
import java.time.LocalDateTime;

public record AlarmResponse(
        long id,
        long memberId,
        String message,
        LocalDateTime createdAt,
        boolean isRead

) {

    public static AlarmResponse of(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getMember().getId(),
                alarm.getMessage(),
                alarm.getCreatedAt(),
                alarm.isRead()
        );
    }

}
