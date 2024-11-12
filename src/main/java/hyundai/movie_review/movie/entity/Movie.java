package hyundai.movie_review.movie.entity;

import hyundai.movie_review.actor.entity.Actor;
import hyundai.movie_review.director.entity.Director;
import hyundai.movie_review.gallery.entity.Gallery;
import hyundai.movie_review.movie_genre.entity.MovieGenre;
import hyundai.movie_review.movie_tag.entity.MovieTag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MOVIE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Movie {

    @Id
    @Column(name = "movie_id")
    private Long id;  // autoincrement 없이 수동 설정

    private String title;

    @Column(columnDefinition = "TEXT")
    private String overview;

    @Column(name = "poster_path")
    private String posterPath;

    @Column(name = "backdrop_path")
    private String backdropPath;

    private Boolean adult;

    @Column(name = "release_date")
    private LocalDateTime releaseDate;

    private int runtime;

    @Column(name = "origin_country")
    private String originCountry;

    @Column(name = "total_review_count")
    private Long totalReviewCount;

    @Column(name = "total_star_rate")
    private Double totalStarRate;

    @OneToMany(mappedBy = "movie")
    private List<Actor> actors;

    @OneToMany(mappedBy = "movie")
    private List<Director> directors;

    @OneToMany(mappedBy = "movie")
    private List<Gallery> galleries;

    @OneToMany(mappedBy = "movie")
    private List<MovieGenre> genres;

    @OneToMany(mappedBy = "movie")
    private List<MovieTag> tags;

    // 리뷰가 생성될 때, 영화에 대한 totalReviewCount, totalStarRate 값을 증가시키는 메소드
    public void increaseTotalValues(Double starRateAmount) {
        this.totalReviewCount += 1L;
        this.totalStarRate += starRateAmount;
    }

    // 리뷰가 삭제될 때, 영화에 대한 totalReviewCount, totalStarRate 값을 감소시키는 메소드
    public void decreaseTotalValues(Double starRateAmount) {
        this.totalReviewCount = Math.max(0, this.totalReviewCount - 1L);
        this.totalStarRate = Math.max(0.0, this.totalStarRate - starRateAmount);
    }
}
