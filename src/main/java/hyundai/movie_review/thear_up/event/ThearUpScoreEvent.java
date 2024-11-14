package hyundai.movie_review.thear_up.event;

import hyundai.movie_review.member.entity.Member;
import org.springframework.context.ApplicationEvent;

public class ThearUpScoreEvent extends ApplicationEvent {

    private static final long THEAR_UP_SCORE_VALUE = 3;
    private final Member member;
    private final long scoreAdjustment;

    public ThearUpScoreEvent(Object source, Member member, boolean isCreated) {
        super(source);
        this.member = member;
        this.scoreAdjustment = isCreated ? THEAR_UP_SCORE_VALUE : -THEAR_UP_SCORE_VALUE;
    }
}
