package hyundai.movie_review.badge.repository;

import hyundai.movie_review.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

}
