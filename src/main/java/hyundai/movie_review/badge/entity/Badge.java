package hyundai.movie_review.badge.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "BADGE")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Badge {
    private Long id;
    private String name;
    private String image;
    private String background_img;
}
