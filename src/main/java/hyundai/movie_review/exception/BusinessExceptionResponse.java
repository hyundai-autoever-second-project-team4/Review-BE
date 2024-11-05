package hyundai.movie_review.exception;

import org.springframework.http.HttpStatus;

public record BusinessExceptionResponse(
        int status,       // HTTP 상태 코드
        String error,     // 에러 타입 (예: "Bad Request")
        String message,   // 에러 메시지 (예: "[ERROR] 해당 member Email은 존재하지 않습니다.")
        String exception  // 예외 클래스명
) {

    public BusinessExceptionResponse(HttpStatus httpStatus, String message, String exception) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), message, exception);
    }
}
