package hyundai.movie_review.review.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewIdNotFoundException extends BusinessException {

    public ReviewIdNotFoundException() {
        super("[ERROR] 해당하는 리뷰 아이디는 존재하지 않습니다.");
    }
}
