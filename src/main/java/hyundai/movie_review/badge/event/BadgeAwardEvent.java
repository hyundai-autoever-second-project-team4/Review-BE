package hyundai.movie_review.badge.event;

import hyundai.movie_review.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BadgeAwardEvent extends ApplicationEvent {

    private final Member member;

    public BadgeAwardEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }
}
