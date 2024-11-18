package hyundai.movie_review.comment.event;

import hyundai.movie_review.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommentScoreEvent extends ApplicationEvent {

    private static final long COMMENT_SCORE_VALUE = 2;
    private final Member giver;
    private final Member receiver;
    private final long scoreAdjustment;
    private final boolean isCreated;

    public CommentScoreEvent(Object source, Member giver, Member receiver, boolean isCreated) {
        super(source);
        this.giver = giver;
        this.receiver = receiver;
        this.scoreAdjustment = isCreated ? COMMENT_SCORE_VALUE : -COMMENT_SCORE_VALUE;
        this.isCreated = isCreated;
    }
}
