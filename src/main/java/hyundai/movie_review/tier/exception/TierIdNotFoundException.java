package hyundai.movie_review.tier.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TierIdNotFoundException extends BusinessException {
    public TierIdNotFoundException() {
        super("[ERROR] 해당 tier Id는 존재하지 않습니다.");
    }
}
