package hyundai.movie_review.score;

import hyundai.movie_review.badge.event.BadgeAwardEvent;
import hyundai.movie_review.comment.event.CommentScoreEvent;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.review.event.ReviewScoreEvent;
import hyundai.movie_review.thear_up.event.ThearUpScoreEvent;
import hyundai.movie_review.tier.constant.TierLevel;
import hyundai.movie_review.tier.entity.Tier;
import hyundai.movie_review.tier.exception.TierIdNotFoundException;
import hyundai.movie_review.tier.repository.TierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScoreEventListener {

    private final TierRepository tierRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    @Transactional
    public void handleReviewScoreEvent(ReviewScoreEvent event) {
        updateMemberTier(event.getMember(), event.getScoreAdjustment());
        log.info("리뷰 이벤트 처리완료");
    }

    @EventListener
    @Transactional
    public void handleCommentScoreEvent(CommentScoreEvent event) {
        updateMemberTier(event.getMember(), event.getScoreAdjustment());
        log.info("댓글 이벤트 처리완료");
    }

    @EventListener
    @Transactional
    public void handleThearUpScoreEvent(ThearUpScoreEvent event) {
        updateMemberTier(event.getMember(), event.getScoreAdjustment());
        log.info("띠어럽 이벤트 처리완료");
    }

    private void updateMemberTier(Member member, long scoreChange) {
        // 점수 업데이트
        long newScore = member.getTotalScore() + scoreChange;
        member.updateScore(newScore);

        // 새로운 티어 결정
        TierLevel newTierLevel = TierLevel.getTierByScore(newScore);

        // Tier Repository에서 tier 정보 가져옴
        Tier newTier = tierRepository.findById(newTierLevel.getId())
                .orElseThrow(TierIdNotFoundException::new);

        // 멤버 정보에 저장
        member.setTier(newTier);

        // 변경된 정보를 저장
        memberRepository.save(member);

        log.info("Member ID: {} - 업데이트된 티어 to: {} 기존 점수 : {}", member.getId(),
                newTier.getName(), newScore);

        applicationEventPublisher.publishEvent(new BadgeAwardEvent(this, member));
    }


}
