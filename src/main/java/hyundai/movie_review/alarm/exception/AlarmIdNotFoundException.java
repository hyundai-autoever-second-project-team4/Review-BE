package hyundai.movie_review.alarm.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AlarmIdNotFoundException extends BusinessException {


    public AlarmIdNotFoundException() {
        super("[ERROR] 해당 알람 아이디는 존재하지 않습니다.");
    }
}
