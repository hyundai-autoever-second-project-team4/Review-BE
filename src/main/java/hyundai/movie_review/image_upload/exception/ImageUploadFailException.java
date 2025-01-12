package hyundai.movie_review.image_upload.exception;

import hyundai.movie_review.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImageUploadFailException extends BusinessException {

    public ImageUploadFailException() {
        super("[ERROR] 프로필 이미지 업로드 실패!");
    }
}
