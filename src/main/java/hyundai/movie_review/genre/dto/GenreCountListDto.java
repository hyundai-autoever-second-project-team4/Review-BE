package hyundai.movie_review.genre.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record GenreCountListDto(
        @Schema(description = "마이페이지 선호 장르 리스트")
        List<GenreCountDto> genreCounts
) {
        public static GenreCountListDto of(List<GenreCountDto> genreCounts){
                return new GenreCountListDto(genreCounts);
        }
}
