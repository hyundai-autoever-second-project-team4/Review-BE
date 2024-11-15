package hyundai.movie_review.member.dto;

import hyundai.movie_review.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

public record MemberProfileUpdateResponse(
        @Schema(description = "프로필 수정된 멤버 id", example = "1")
        Long memberId,

        @Schema(description = "수정 완료 메시지", example = "프로필 정보를 수정했습니다.")
        String message
) {
    public static MemberProfileUpdateResponse of(Long memberId){
        return new MemberProfileUpdateResponse(memberId, "프로필 정보를 수정했습니다.");
    }
}
