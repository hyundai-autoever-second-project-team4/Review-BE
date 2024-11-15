package hyundai.movie_review.search.service;

import hyundai.movie_review.movie.dto.MovieWithGenreInfoDto;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.repository.MovieRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final MovieRepository movieRepository;

    public Page<MovieWithGenreInfoDto> searchMoviesByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Page<Movie>를 가져온 후 DTO로 변환
        Page<Movie> moviesPage = movieRepository.findByTitleIgnoringSpaces(title, pageable);

        // Movie -> MovieWithGenreInfoDto로 변환
        List<MovieWithGenreInfoDto> movieDtos = moviesPage.getContent().stream()
                .map(MovieWithGenreInfoDto::of)
                .collect(Collectors.toList());

        return new PageImpl<>(movieDtos, pageable, moviesPage.getTotalElements());
    }

    public Page<MovieWithGenreInfoDto> searchMovieByGenre(String genre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // Page<Movie>를 가져온 후 DTO로 변환
        Page<Movie> moviesPage = movieRepository.findMoviesByGenreName(genre, pageable);

        // Movie -> MovieWithGenreInfoDto로 변환
        List<MovieWithGenreInfoDto> movieDtos = moviesPage.getContent().stream()
                .map(MovieWithGenreInfoDto::of)
                .collect(Collectors.toList());

        return new PageImpl<>(movieDtos, pageable, moviesPage.getTotalElements());
    }

}
