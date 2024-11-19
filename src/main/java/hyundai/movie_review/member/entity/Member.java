package hyundai.movie_review.member.entity;

import hyundai.movie_review.alarm.entity.Alarm;
import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.member_badge.entity.MemberBadge;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.security.model.MemberRole;
import hyundai.movie_review.thear_down.entity.ThearDown;
import hyundai.movie_review.thear_up.entity.ThearUp;
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

    // badge와 연결
    @ManyToOne
    @JoinColumn(name = "badge_id")
    private Badge badge;

    // tier와 연결
    @ManyToOne
    @JoinColumn(name = "tier_id")
    private Tier tier;

    private String email;
    private String name;
    @Lob
    @Column(name = "profile_image", columnDefinition = "TEXT")
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

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public void setBadge(Badge badge) {
        this.badge = badge;
    }

    public void updateScore(long score) {
        this.totalScore = score;
    }

    // 받은 ThearUp 수 계산
    public long getReceivedThearUpCount() {
        return reviews.stream()
                .filter(review -> !review.getDeleted())
                .mapToLong(Review::getThearUps)
                .sum();
    }

    // MemberBadge와 연결
    @OneToMany(mappedBy = "memberId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<MemberBadge> memberBadges;

    // Comment와 연결
    @OneToMany(mappedBy = "memberId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments;

    // ThearUp과 연결
    @OneToMany(mappedBy = "memberId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ThearUp> thearUps;

    // ThearDown과 연결
    @OneToMany(mappedBy = "memberId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<ThearDown> thearDowns;

    // Review와 연결
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Review> reviews;

    // Alarm과 연결
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Alarm> alarms;
}
