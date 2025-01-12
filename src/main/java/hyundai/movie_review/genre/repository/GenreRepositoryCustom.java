package hyundai.movie_review.genre.repository;

import hyundai.movie_review.genre.dto.GenreCountListDto;
import hyundai.movie_review.member.entity.Member;

public interface GenreRepositoryCustom {
    GenreCountListDto getGenreCountByMember(Member member);
}
