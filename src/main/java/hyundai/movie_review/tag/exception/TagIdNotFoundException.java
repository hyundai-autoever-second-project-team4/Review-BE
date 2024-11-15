package hyundai.movie_review.tag.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TagIdNotFoundException extends BusinessException {
    public TagIdNotFoundException() {
        super("[ERROR] 해당 tag id는 존재하지 않습니다.");
    }


}
