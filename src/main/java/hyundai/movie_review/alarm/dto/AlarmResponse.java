package hyundai.movie_review.alarm.dto;

import hyundai.movie_review.alarm.entity.Alarm;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Description;

public record AlarmResponse(
        long id,
        long memberId,
        String message,
        LocalDateTime createdAt,
        @Schema(example = "false")
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
