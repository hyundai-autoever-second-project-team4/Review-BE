package hyundai.movie_review.movie_tag.repository;

import hyundai.movie_review.movie_tag.entity.MovieTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieTagRepository extends JpaRepository<MovieTag, Long> {


}
