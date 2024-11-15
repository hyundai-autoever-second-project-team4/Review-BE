package hyundai.movie_review.movie.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MovieReviewTypeNotFound extends BusinessException {
    public MovieReviewTypeNotFound() { super("[ERROR] 해당하는 영화 리뷰 정렬 타입은 존재하지 않습니다. latest, likes, ratingHigh, ratingLow, comments 중 택1"); }
}
