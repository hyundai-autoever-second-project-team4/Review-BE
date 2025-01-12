package hyundai.movie_review.actor.dto;

import hyundai.movie_review.actor.entity.Actor;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "영화 배우의 리스트 DTO")
public record ActorInfoListDto(
        @Schema(description = "영화에 출연하는 배우들의 전체 리스트")
        List<ActorInfoDto> actors
) {

    public static ActorInfoListDto of(List<Actor> actorList) {
        List<ActorInfoDto> actorInfoDtos =
                actorList.stream()
                        .map(ActorInfoDto::of)
                        .toList();

        return new ActorInfoListDto(actorInfoDtos);
    }


}
