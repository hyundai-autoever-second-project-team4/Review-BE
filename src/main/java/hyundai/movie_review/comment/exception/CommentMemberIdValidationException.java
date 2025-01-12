package hyundai.movie_review.comment.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)

public class CommentMemberIdValidationException extends BusinessException {

    public CommentMemberIdValidationException() {
        super("[ERROR] 사용자가 작성한 댓글이 아닙니다.");
    }
}
