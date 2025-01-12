package hyundai.movie_review.thear_up.event;

import hyundai.movie_review.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ThearUpScoreEvent extends ApplicationEvent {

    private static final long THEAR_UP_SCORE_VALUE = 3;
    private final Member giver;
    private final Member receiver;
    private final long scoreAdjustment;
    private boolean isCreated;

    public ThearUpScoreEvent(Object source, Member giver, Member receiver, boolean isCreated) {
        super(source);
        this.giver = giver;
        this.receiver = receiver;
        this.scoreAdjustment = isCreated ? THEAR_UP_SCORE_VALUE : -THEAR_UP_SCORE_VALUE;
        this.isCreated = isCreated;
    }
}
