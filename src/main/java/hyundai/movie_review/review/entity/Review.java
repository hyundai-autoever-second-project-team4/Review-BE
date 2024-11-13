package hyundai.movie_review.review.entity;

import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.thear_down.entity.ThearDown;
import hyundai.movie_review.thear_up.entity.ThearUp;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "REVIEW")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "movie_id")
    private Long movieId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "star_rate")
    private Double starRate;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean spoiler;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    public void delete() {
        this.deleted = true;
    }

    // Comment와 연결
    @OneToMany(mappedBy = "reviewId", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    // ThearUp과 연결
    @OneToMany(mappedBy = "reviewId", cascade = CascadeType.REMOVE)
    private List<ThearUp> thearUps;

    // ThearDown과 연결
    @OneToMany(mappedBy = "reviewId", cascade = CascadeType.REMOVE)
    private List<ThearDown> thearDowns;

}
