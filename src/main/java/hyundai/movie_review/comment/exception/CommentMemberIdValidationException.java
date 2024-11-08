package hyundai.movie_review.comment.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)

public class CommentMemberIdValidationException extends BusinessException {

    public CommentMemberIdValidationException() {
        super("[ERROR] 자신이 작성한 댓글만 삭제할 수 있습니다.");
    }
}
