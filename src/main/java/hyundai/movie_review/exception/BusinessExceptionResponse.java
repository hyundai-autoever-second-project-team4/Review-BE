package hyundai.movie_review.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;

@Schema(description = "비즈니스 예외 응답 모델")
public record BusinessExceptionResponse(

        @Schema(description = "HTTP 상태 코드")
        int status,

        @Schema(description = "에러 타입", example = "Bad Request, Unauthorized, ...")
        String error,

        @Schema(description = "예외에 대한 상세 메시지", example = "[ERROR] 에러 메세지 ...")
        String message,

        @Schema(description = "예외 클래스명", example = "BusinessException")
        String exception
) {

    public BusinessExceptionResponse(HttpStatus httpStatus, String message, String exception) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), message, exception);
    }
}
