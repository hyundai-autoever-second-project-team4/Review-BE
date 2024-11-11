package hyundai.movie_review.genre.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "영화에 대한 장르 리스트 DTO")
public record GenreInfoListDto(
        @Schema(description = "영화에 대한 장르 리스트")
        List<GenreInfoDto> genres
) {

}
