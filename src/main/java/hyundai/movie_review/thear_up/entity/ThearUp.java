package hyundai.movie_review.thear_up.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "THEAR_UP")
public class ThearUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "review_id")
    private Long reviewId;

    public ThearUp(Long memberId, Long reviewId){
        this.memberId = memberId;
        this.reviewId = reviewId;
    }
}
