package hyundai.movie_review.alarm.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AlarmNotAuthorizedException extends BusinessException {

    public AlarmNotAuthorizedException() {
        super("[ERROR] 알람의 member Id와 현재 member id가 일치하지 않습니다.");
    }
}
