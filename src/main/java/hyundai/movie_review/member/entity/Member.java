package hyundai.movie_review.member.entity;

import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.member_badge.entity.MemberBadge;
import hyundai.movie_review.security.model.MemberRole;
import hyundai.movie_review.tier.entity.Tier;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMBER")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "badge_id")
    private Badge badge;

    @ManyToOne
    @JoinColumn(name = "tier_id")
    private Tier tier;

    private String email;
    private String name;
    @Column(name = "profile_image")
    private String profileImage;
    private String social;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "total_score")
    private Long totalScore;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> memberRoles = new HashSet<>();


    public void addRole(MemberRole role) {
        this.memberRoles.add(role);
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    // MemberBadge entity와 연결
    @OneToMany(mappedBy = "memberId", cascade = CascadeType.REMOVE)
    private List<MemberBadge> memberBadges;
}
