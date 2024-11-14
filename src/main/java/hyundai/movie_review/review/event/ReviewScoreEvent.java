package hyundai.movie_review.review.event;

import hyundai.movie_review.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReviewScoreEvent extends ApplicationEvent {

    private static final long REVIEW_SCORE_VALUE = 10;

    private final Member member;
    private final long scoreAdjustment;

    public ReviewScoreEvent(Object source, Member member, boolean isCreated) {
        super(source);
        this.member = member;
        this.scoreAdjustment = isCreated ? REVIEW_SCORE_VALUE : -REVIEW_SCORE_VALUE;
    }
}
