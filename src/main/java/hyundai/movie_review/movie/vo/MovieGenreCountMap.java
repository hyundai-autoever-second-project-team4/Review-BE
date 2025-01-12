package hyundai.movie_review.movie.vo;

import java.util.HashMap;
import java.util.Map;
import lombok.ToString;

@ToString
public class MovieGenreCountMap {

    private final Map<Long, Integer> movieGenreCountMap = new HashMap<>();

    public void addGenreCount(long genreId) {
        movieGenreCountMap.put(
                genreId,
                movieGenreCountMap.getOrDefault(genreId, 0) + 1
        );
    }

    public Long getMostCountedGenreId() {
        return movieGenreCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(1L); // 값이 없는 경우 기본값 처리
    }


}
