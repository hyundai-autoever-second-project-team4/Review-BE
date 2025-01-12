package hyundai.movie_review.badge.entity;

import hyundai.movie_review.member_badge.entity.MemberBadge;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity(name = "BADGE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Badge {

    @Id
    private Long id;
    private String name;
    private String image;
    private String background_img;

    @OneToMany(mappedBy = "badgeId", cascade = CascadeType.REMOVE)
    private List<MemberBadge> memberBadges;

    public long getMemberBadgesCount(){
        return memberBadges.isEmpty() ? 0 : memberBadges.size();
    }
}
