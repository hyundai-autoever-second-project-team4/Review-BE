package hyundai.movie_review.thear_down.event;

import hyundai.movie_review.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ThearDownScoreEvent extends ApplicationEvent {

    private static final long THEAR_DOWN_SCORE_VALUE = 0;
    private final Member giver;
    private final Member receiver;
    private final long scoreAdjustment;
    private boolean isCreated;

    public ThearDownScoreEvent(Object source, Member giver, Member receiver, boolean isCreated) {
        super(source);
        this.giver = giver;
        this.receiver = receiver;
        this.scoreAdjustment = isCreated ? THEAR_DOWN_SCORE_VALUE : -THEAR_DOWN_SCORE_VALUE;
        this.isCreated = isCreated;
    }

}
