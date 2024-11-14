package hyundai.movie_review.review.event;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.entity.Review;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReviewCreateEvent extends ApplicationEvent {

    private final Member member;
    private final Review review;

    public ReviewCreateEvent(Object source, Review review, Member member) {
        super(source);
        this.review = review;
        this.member = member;
    }

}
