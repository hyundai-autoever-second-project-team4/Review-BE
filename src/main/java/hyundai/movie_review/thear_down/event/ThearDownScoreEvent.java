package hyundai.movie_review.thear_down.event;

import hyundai.movie_review.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ThearDownScoreEvent extends ApplicationEvent {

    private static final long THEAR_DOWN_SCORE_VALUE = 0;
    private final Member member;
    private final long scoreAdjustment;

    public ThearDownScoreEvent(Object source, Member member, boolean isCreated) {
        super(source);
        this.member = member;
        this.scoreAdjustment = isCreated ? THEAR_DOWN_SCORE_VALUE : -THEAR_DOWN_SCORE_VALUE;
    }

}
