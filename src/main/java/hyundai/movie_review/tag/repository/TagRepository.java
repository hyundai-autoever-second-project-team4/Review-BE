package hyundai.movie_review.tag.repository;

import hyundai.movie_review.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {

}
