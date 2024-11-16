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

        // 상위 3개 태그 DTO 변환
        List<TagInfoDto> topTags = movieTags.stream()
                .filter(tag -> topTagIds.contains(tag.getTag().getId()))
                .map(TagInfoDto::of)
                .toList();

        return new TagInfoListDto(topTags);
    }
}
