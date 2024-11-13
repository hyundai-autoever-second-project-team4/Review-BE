package hyundai.movie_review.comment.entity;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.entity.Review;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "COMMENT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Member와 연결
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member memberId;

    // Review와 연결
    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review reviewId;

    private String content;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public void changeContent(String content){
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }
}
