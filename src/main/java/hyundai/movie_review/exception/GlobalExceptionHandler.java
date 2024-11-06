package hyundai.movie_review.exception;

import hyundai.movie_review.security.exception.MemberAuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // BusinessException 예외 처리
    // BusinessException 및 하위 클래스 처리
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BusinessExceptionResponse> handleBusinessException(BusinessException ex) {
        // 예외 클래스에서 HTTP 상태를 가져오고, ExceptionResponseDto로 응답
        HttpStatus status = ex.getClass().getAnnotation(ResponseStatus.class).value();
        BusinessExceptionResponse response = new BusinessExceptionResponse(status, ex.getMessage(), ex.getClass().getSimpleName());
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(MemberAuthenticationException.class)
    public ResponseEntity<BusinessExceptionResponse> handleMemberAuthenticationException(MemberAuthenticationException ex) {
        // 예외 클래스에서 HTTP 상태를 가져오고, ExceptionResponseDto로 응답
        HttpStatus status = ex.getClass().getAnnotation(ResponseStatus.class).value();
        BusinessExceptionResponse response = new BusinessExceptionResponse(status, ex.getMessage(), ex.getClass().getSimpleName());
        return new ResponseEntity<>(response, status);
    }
}
