package hyundai.movie_review.gallery.entity.dto;

import hyundai.movie_review.gallery.entity.Gallery;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "영화에 대한 갤러리 DTO")
public record GalleryInfoDto(
        @Schema(description = "갤러리가 속한 영화 ID", example = "1")
        long movieId,
        @Schema(description = "갤러리의 이미지 URL", example = "/gMQibswELoKmB60imE7WFMlCuqY.jpg")
        String galleryPath

) {

    public static GalleryInfoDto of(Gallery gallery) {
        return new GalleryInfoDto(
                gallery.getMovieId(),
                gallery.getGalleryPath()
        );
    }

}
