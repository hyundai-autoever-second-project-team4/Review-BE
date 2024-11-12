package hyundai.movie_review.member_badge.entity;

import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "MEMBER_BADGE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MemberBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne
    @JoinColumn(name = "badge_id")
    private Badge badgeId;
}
