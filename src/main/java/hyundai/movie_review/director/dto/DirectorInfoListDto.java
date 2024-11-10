package hyundai.movie_review.director.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "영화 감독 리스트 DTO")
public record DirectorInfoListDto(
        @Schema(description = "영화 감독 리스트 정보")
        List<DirectorInfoDto> directors
) {

}
