package hyundai.movie_review.member_badge.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemberBadgeNotFoundException extends BusinessException {
    public MemberBadgeNotFoundException() {super("[ERROR] 현재 멤버는 해당 뱃지를 획득하지 않았습니다.");}
}
