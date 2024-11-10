package hyundai.movie_review.movie.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieIdNotFoundException extends BusinessException {

    public MovieIdNotFoundException() {
        super("[ERROR] 해당하는 movie id는 존재하지 않습니다.");
    }
}
