package hyundai.movie_review.comment.event;

import hyundai.movie_review.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommentScoreEvent extends ApplicationEvent {

    private static final long COMMENT_SCORE_VALUE = 2;
    private final Member member;
    private final long scoreAdjustment;

    public CommentScoreEvent(Object source, Member member, boolean isCreated) {
        super(source);
        this.member = member;
        this.scoreAdjustment = isCreated ? COMMENT_SCORE_VALUE : -COMMENT_SCORE_VALUE;
    }
}
