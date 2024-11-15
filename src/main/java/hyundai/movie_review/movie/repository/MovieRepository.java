package hyundai.movie_review.movie.repository;

import hyundai.movie_review.movie.entity.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {

    @Query("SELECT m FROM Movie m WHERE REPLACE(LOWER(m.title), ' ', '') LIKE LOWER(CONCAT('%', REPLACE(:title, ' ', ''), '%'))")
    Page<Movie> findByTitleIgnoringSpaces(@Param("title") String title, Pageable pageable);

}
