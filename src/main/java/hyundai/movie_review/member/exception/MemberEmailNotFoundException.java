package hyundai.movie_review.member.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberEmailNotFoundException extends BusinessException {

    public MemberEmailNotFoundException() {
        super("[ERROR] 해당 member Email은 존재하지 않습니다.");
    }
}
