package hyundai.movie_review.review.entity;

import hyundai.movie_review.comment.entity.Comment;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.thear_down.entity.ThearDown;
import hyundai.movie_review.thear_up.entity.ThearUp;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "REVIEW")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Where(clause = "deleted = false")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

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
    public long getCommentCounts(){ return comments != null ? (long) comments.size() : 0;}
    public long getThearUps(){
        if(thearUps != null) return (long) thearUps.size();
        else return 0L;
        //return thearUps != null ? (long) thearUps.size() : 0L;
    }
    public long getThearDowns(){
        if(thearDowns != null) return (long) thearDowns.size();
        else return 0L;
        //return thearDowns !=  null ? (long) thearDowns.size() : 0L;
    }
}
