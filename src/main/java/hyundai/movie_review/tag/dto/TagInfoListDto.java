package hyundai.movie_review.tag.dto;

import hyundai.movie_review.movie_tag.entity.MovieTag;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "영화에 대한 태그 정보 리스트 DTO")
public record TagInfoListDto(
        @Schema(description = "영화에 해당하는 태그 전체 리스트")
        List<TagInfoDto> tags
) {

    public static TagInfoListDto of(List<MovieTag> tags) {
        List<TagInfoDto> tagInfoDtos = tags.stream()
                .map(TagInfoDto::of)
                .toList();

        return new TagInfoListDto(tagInfoDtos);
    }
}
