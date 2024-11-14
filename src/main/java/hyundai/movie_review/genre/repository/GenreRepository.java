package hyundai.movie_review.genre.repository;

import hyundai.movie_review.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long>, GenreRepositoryCustom {

}
