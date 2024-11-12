package hyundai.movie_review.review.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ReviewAuthorMismatchException extends BusinessException {

    public ReviewAuthorMismatchException() {
        super("[ERROR] 리뷰 작성자와 현재 사용자가 일치하지 않습니다.");
    }
}
