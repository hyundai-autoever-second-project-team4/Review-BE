package hyundai.movie_review.thear_up.entity;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "THEAR_UP")
public class ThearUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review reviewId;
}
