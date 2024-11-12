package hyundai.movie_review.gallery.entity.dto;

import hyundai.movie_review.gallery.entity.Gallery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "영화에 대한 갤러리 리스트 DTO")
public record GalleryInfoListDto(
        @Schema(description = "영화에 해당하는 이미지 전체 리스트")
        List<GalleryInfoDto> galleries

) {

    public static GalleryInfoListDto of(List<Gallery> galleries) {
        List<GalleryInfoDto> galleryInfoDtos = galleries.stream()
                .map(GalleryInfoDto::of)
                .toList();

        return new GalleryInfoListDto(galleryInfoDtos);
    }
}
