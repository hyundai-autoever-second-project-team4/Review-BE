package hyundai.movie_review.tag.dto;

import hyundai.movie_review.movie_tag.entity.MovieTag;
import hyundai.movie_review.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "영화에 대한 태그 DTO")
public record TagInfoDto(

        @Schema(description = "태그 id", example = "1")
        long tagId,
        @Schema(description = "태그 내용", example = "연인과 함께")
        String content
) {

    public static TagInfoDto of(MovieTag movieTag) {
        return new TagInfoDto(
                movieTag.getTag().getId(),
                movieTag.getTag().getContent()
        );
    }

}
