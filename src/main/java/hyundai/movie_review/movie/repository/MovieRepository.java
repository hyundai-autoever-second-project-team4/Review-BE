package hyundai.movie_review.movie.repository;

import hyundai.movie_review.movie.entity.Movie;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long>, MovieRepositoryCustom {

}
