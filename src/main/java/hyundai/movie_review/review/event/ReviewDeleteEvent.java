package hyundai.movie_review.review.event;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.entity.Review;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReviewDeleteEvent extends ApplicationEvent {

    private final Member member;
    private final Review review;

    public ReviewDeleteEvent(Object source, Member member, Review review) {
        super(source);
        this.member = member;
        this.review = review;
    }

}
