package hyundai.movie_review.actor.dto;

import hyundai.movie_review.actor.entity.Actor;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "영화 배우 정보 DTO")
public record ActorInfoDto(
        @Schema(description = "배우의 ID", example = "1")
        long actorId,
        @Schema(description = "배우가 출연한 영화의 ID", example = "1")
        long movieId,
        @Schema(description = "배우의 이름", example = "이승우")
        String name,
        @Schema(description = "배우의 극중 이름", example = "고니")
        String characterName,
        @Schema(description = "배우의 프로필 이미지", example = "/qJYWq2oZcvHh7lnGskxkrYXCom0.jpg")
        String profilePath
) {

    public static ActorInfoDto of(Actor actor) {
        return new ActorInfoDto(
                actor.getId(),
                actor.getMovie().getId(),
                actor.getName(),
                actor.getCharacterName(),
                actor.getProfilePath()
        );
    }

}
