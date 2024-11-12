package hyundai.movie_review.badge.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BadgeIdNotFoundException extends BusinessException {

    public BadgeIdNotFoundException() {
        super("[ERROR] 해당하는 뱃지 아이디는 존재하지 않습니다.");
    }
}
