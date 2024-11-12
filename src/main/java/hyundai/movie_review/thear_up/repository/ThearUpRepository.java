package hyundai.movie_review.thear_up.repository;

import hyundai.movie_review.thear_up.entity.ThearUp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThearUpRepository extends JpaRepository<ThearUp,Long> {
    
}
