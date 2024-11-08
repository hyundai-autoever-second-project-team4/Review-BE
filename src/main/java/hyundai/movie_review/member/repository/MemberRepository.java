package hyundai.movie_review.member.repository;

import hyundai.movie_review.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @EntityGraph(attributePaths = "memberRoles")
    Optional<Member> findByEmail(String email);
}
