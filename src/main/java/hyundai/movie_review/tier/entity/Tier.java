package hyundai.movie_review.tier.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "TIER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Tier {
    @Id
    private Long id;
    private String name;
    private String image;
}
