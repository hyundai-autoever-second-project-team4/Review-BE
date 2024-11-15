package hyundai.movie_review.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "수정할 사용자 정보")
public record MemberProfileUpdateRequest(
        @Schema(description = "변경할 멤버 이름", example = "영화 팬")
        String memberName,

        @Schema(description = "변경할 멤버 프로필 사진")
        MultipartFile memberProfileImg,

        @Schema(description = "대표 뱃지 id", example = "2")
        Long primaryBadgeId
) {
}
