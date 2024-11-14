package hyundai.movie_review.genre.repository;

import hyundai.movie_review.genre.dto.GenreCountDto;
import hyundai.movie_review.genre.dto.GenreCountListDto;
import hyundai.movie_review.member.entity.Member;

import java.util.List;
import java.util.stream.Collectors;

public class GenreRepositoryImpl implements GenreRepositoryCustom{
    @Override
    public GenreCountListDto getGenreCountByMember(Member member) {
        List<GenreCountDto> genreCounts = member.getReviews().stream()
                .flatMap(review -> review.getMovie().getGenres().stream())
                .collect(Collectors.groupingBy(movieGenre -> movieGenre.getGenre().getName(), Collectors.counting()))
                .entrySet()
                .stream().map(entry -> new GenreCountDto(entry.getKey(), entry.getValue()))
                .toList();

        return GenreCountListDto.of(genreCounts);
    }
}
