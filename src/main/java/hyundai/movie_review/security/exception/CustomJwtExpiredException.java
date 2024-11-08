package hyundai.movie_review.security.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)

public class CustomJwtExpiredException extends BusinessException {

    public CustomJwtExpiredException(String msg) {
        super(msg);
    }
}
