package hyundai.movie_review.score;

import hyundai.movie_review.alarm.entity.Alarm;
import hyundai.movie_review.alarm.service.AlarmService;
import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.badge.event.BadgeAwardEvent;
import hyundai.movie_review.comment.event.CommentScoreEvent;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.review.event.ReviewScoreEvent;
import hyundai.movie_review.thear_down.event.ThearDownScoreEvent;
import hyundai.movie_review.thear_up.entity.ThearUp;
import hyundai.movie_review.thear_up.event.ThearUpScoreEvent;
import hyundai.movie_review.tier.constant.TierLevel;
import hyundai.movie_review.tier.entity.Tier;
import hyundai.movie_review.tier.exception.TierIdNotFoundException;
import hyundai.movie_review.tier.repository.TierRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
    private final AlarmService alarmService;

    @EventListener
    @Transactional
    public void handleReviewScoreEvent(ReviewScoreEvent event) {
        updateMemberTier(event.getMember(), event.getScoreAdjustment());

        // 리뷰 작성자에 대한 뱃지 이벤트
        BadgeAwardEvent badgeAwardEvent = new BadgeAwardEvent(this, event.getMember());
        applicationEventPublisher.publishEvent(badgeAwardEvent);

        log.info("리뷰 이벤트 처리완료");
    }

    @EventListener
    @Transactional
    public void handleCommentScoreEvent(CommentScoreEvent event) {
        updateMemberTier(event.getGiver(), event.getScoreAdjustment());

        // 댓글받은 사람에 대한 뱃지 이벤트
        BadgeAwardEvent badgeAwardEvent = new BadgeAwardEvent(this, event.getReceiver());
        applicationEventPublisher.publishEvent(badgeAwardEvent);

        if (event.isCreated()) {
            Alarm alarm = createReceiverAlarm(event.getGiver(), event.getReceiver(),
                    "님이 내 글에 댓글을 남겼습니다.");

            alarmService.sendNotificationToUser(event.getReceiver().getId(), alarm);

            log.info("댓글 이벤트 처리완료");
        }
    }

    @EventListener
    @Transactional
    public void handleThearUpScoreEvent(ThearUpScoreEvent event) {
        updateMemberTier(event.getReceiver(), event.getScoreAdjustment());

        // thearup 받은 사람에 대한 뱃지 이벤트
        BadgeAwardEvent badgeAwardEvent = new BadgeAwardEvent(this, event.getReceiver());
        applicationEventPublisher.publishEvent(badgeAwardEvent);

        if (event.isCreated()) {
            Alarm alarm = createReceiverAlarm(event.getGiver(), event.getReceiver(),
                    "님에게 '띠어럽' 을 받았습니다.");
            alarmService.sendNotificationToUser(event.getReceiver().getId(), alarm);

            log.info("띠어럽 이벤트 처리완료");
        }
    }

    @EventListener
    @Transactional
    public void handleThearDownScoreEvent(ThearDownScoreEvent event) {
        updateMemberTier(event.getReceiver(), event.getScoreAdjustment());

        // theardown 받은 사람에 대한 뱃지 이벤트
        BadgeAwardEvent badgeAwardEvent = new BadgeAwardEvent(this, event.getReceiver());
        applicationEventPublisher.publishEvent(badgeAwardEvent);

        if (event.isCreated()) {
            Alarm alarm = createReceiverAlarm(event.getGiver(), event.getReceiver(),
                    "님에게 '띠어다운' 을 받았습니다.");
            alarmService.sendNotificationToUser(event.getReceiver().getId(), alarm);

            log.info("띠어다운 이벤트 처리 완료");
        }
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

        // 기존 티어와 새로운 티어 비교
        Tier currentTier = member.getTier();

        if (!newTier.equals(currentTier)) {
            // 티어가 변경된 경우
            member.setTier(newTier);

            // 변경된 정보를 저장
            memberRepository.save(member);

            log.info("Member ID: {} - 티어 변경: {} -> {} (점수: {})",
                    member.getId(), currentTier.getName(), newTier.getName(), newScore);

            // 티어가 변경되었을 때, 알람 발생
            Alarm alarm = createTierAlarm(member, newTier);
            alarmService.sendNotificationToUser(member.getId(), alarm);

        } else {
            // 티어가 변경되지 않은 경우
            log.info("Member ID: {} - 티어 유지: {} (점수: {})",
                    member.getId(), currentTier.getName(), newScore);
        }
    }

    private Alarm createTierAlarm(Member member, Tier tier) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[MM월 dd일 HH:mm:ss]");
        String formattedDateTime = now.format(formatter);

        String message = String.format("%s '%s' 티어로 변경되었습니다.", formattedDateTime, tier.getName());

        log.info("티어 알람 생성 : [{}]", message);

        return Alarm.builder()
                .createdAt(now)
                .member(member)
                .message(message)
                .isRead(false)
                .build();
    }

    private Alarm createReceiverAlarm(Member giver, Member receiver, String msg) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[MM월 dd일 HH:mm:ss]");
        String formattedDateTime = now.format(formatter);

        String message = String.format("%s '%s' %s", formattedDateTime, giver.getName(), msg);

        log.info("수신자 알람 생성 : [{}]", message);

        return Alarm.builder()
                .createdAt(now)
                .member(receiver)
                .message(message)
                .isRead(false)
                .build();
    }

}
