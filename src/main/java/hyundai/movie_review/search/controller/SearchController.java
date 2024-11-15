package hyundai.movie_review.search.controller;


import hyundai.movie_review.movie.dto.MovieWithGenreInfoDto;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.search.service.SearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Tag", description = "영화 검색 관련 API")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search/movie")
    public ResponseEntity<?> searchMovieByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<MovieWithGenreInfoDto> response = searchService.searchMoviesByTitle(title, page, size);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/genre")
    public ResponseEntity<?> searchMovieByActor(
            @RequestParam String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<MovieWithGenreInfoDto> response = searchService.searchMovieByGenre(genre, page, size);

        return ResponseEntity.ok(response);
    }
}
