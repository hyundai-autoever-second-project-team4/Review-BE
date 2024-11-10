package hyundai.movie_review.tag.dto;

import hyundai.movie_review.tag.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "영화에 대한 태그 DTO")
public record TagInfoDto(

        @Schema(description = "태그 내용", example = "연인과 함께")
        String content,
        @Schema(description = "태그 이미지", example = "./heart.jpg")
        String img
) {

    public static TagInfoDto of(Tag tag) {
        return new TagInfoDto(
                tag.getContent(),
                tag.getImg()
        );
    }
}
