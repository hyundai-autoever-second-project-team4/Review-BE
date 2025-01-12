package hyundai.movie_review.comment.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentIdNotFoundException extends BusinessException {
    public CommentIdNotFoundException() {
        super("[ERROR] 해당 comment id는 존재하지 않습니다.");
    }
}
