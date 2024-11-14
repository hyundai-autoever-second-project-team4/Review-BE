package hyundai.movie_review.member_badge.repository;

import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member_badge.entity.MemberBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberBadgeRepository extends JpaRepository<MemberBadge, Long> {

    boolean existsByMemberIdAndBadgeId(Member member, Badge badge);

}
