package hyundai.movie_review.director.dto;

import hyundai.movie_review.director.entity.Director;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "영화 감독 리스트 DTO")
public record DirectorInfoListDto(
        @Schema(description = "영화 감독 리스트 정보")
        List<DirectorInfoDto> directors
) {

    public static DirectorInfoListDto of(List<Director> directors) {
        List<DirectorInfoDto> directorInfoDtos = directors.stream()
                .map(DirectorInfoDto::of)
                .toList();

        return new DirectorInfoListDto(directorInfoDtos);
    }
}
