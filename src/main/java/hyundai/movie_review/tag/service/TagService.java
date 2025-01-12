package hyundai.movie_review.tag.service;

import hyundai.movie_review.movie_tag.entity.MovieTag;
import hyundai.movie_review.tag.dto.TagInfoDto;
import hyundai.movie_review.tag.dto.TagInfoListDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {

    public TagInfoListDto getTopTags(List<MovieTag> movieTags) {
        // 태그 ID별 카운트
        Map<Long, Long> tagCountMap = movieTags.stream()
                .collect(Collectors.groupingBy(tag -> tag.getTag().getId(), Collectors.counting()));

        // 카운트 기준으로 내림차순 정렬 후 상위 3개 선택
        List<Long> topTagIds = tagCountMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .toList();

        // 상위 3개 태그 ID에 해당하는 태그를 그룹화하고, 첫 번째 태그만 DTO로 변환
        List<TagInfoDto> topTags = movieTags.stream()
                .filter(tag -> topTagIds.contains(tag.getTag().getId()))
                .collect(Collectors.groupingBy(tag -> tag.getTag().getId()))
                .values().stream()
                .map(tags -> TagInfoDto.of(tags.get(0))) // 그룹의 첫 번째 태그만 선택
                .toList();

        return new TagInfoListDto(topTags);
    }
}
