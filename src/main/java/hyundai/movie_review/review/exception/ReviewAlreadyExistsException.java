package hyundai.movie_review.review.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)

public class ReviewAlreadyExistsException extends BusinessException {

    public ReviewAlreadyExistsException() {
        super("[ERROR] 이미 작성된 리뷰가 있습니다. 리뷰는 영화당 1개만 작성 가능합니다.");
    }
}
