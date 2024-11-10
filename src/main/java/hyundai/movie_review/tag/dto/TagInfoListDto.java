package hyundai.movie_review.tag.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "영화에 대한 태그 정보 리스트 DTO")
public record TagInfoListDto(
        @Schema(description = "영화에 해당하는 태그 전체 리스트")
        List<TagInfoDto> tags
) {

}
