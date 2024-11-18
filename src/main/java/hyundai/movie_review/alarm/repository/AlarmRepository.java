package hyundai.movie_review.alarm.repository;

import hyundai.movie_review.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Long, Alarm> {
    

}
