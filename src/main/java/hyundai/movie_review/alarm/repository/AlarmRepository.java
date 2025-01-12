package hyundai.movie_review.alarm.repository;

import hyundai.movie_review.alarm.entity.Alarm;
import hyundai.movie_review.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findAllByMemberId(long memberId);

}
