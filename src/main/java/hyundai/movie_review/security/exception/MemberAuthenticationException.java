package hyundai.movie_review.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)  // 예외에 HTTP 상태 코드를 명시
public class MemberAuthenticationException extends RuntimeException{
    public MemberAuthenticationException() {
        super("[ERROR] 인증되지 않은 사용자입니다.");
    }
}
