package hyundai.movie_review.security.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomJwtException extends BusinessException {

    public CustomJwtException(String msg) {
        super(msg);
    }
}
