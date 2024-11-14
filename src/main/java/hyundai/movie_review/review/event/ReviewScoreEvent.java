package hyundai.movie_review.review.event;

import hyundai.movie_review.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReviewScoreEvent extends ApplicationEvent {

    private final Member member;
    private final long scoreChange;

    public ReviewScoreEvent(Object source, Member member, long scoreChange) {
        super(source);
        this.member = member;
        this.scoreChange = scoreChange;
    }
}
