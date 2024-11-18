package hyundai.movie_review.badge.event;

import hyundai.movie_review.alarm.entity.Alarm;
import hyundai.movie_review.alarm.repository.AlarmRepository;
import hyundai.movie_review.alarm.service.AlarmService;
import hyundai.movie_review.badge.constant.BadgeConditionByBadgeCount;
import hyundai.movie_review.badge.constant.BadgeConditionByGenreCount;
import hyundai.movie_review.badge.constant.BadgeConditionBySpecificReview;
import hyundai.movie_review.badge.constant.BadgeConditionByThearDownCount;
import hyundai.movie_review.badge.constant.BadgeConditionByThearUpCount;
import hyundai.movie_review.badge.constant.BadgeConditionByTotalReviewCount;
import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.badge.exception.BadgeIdNotFoundException;
import hyundai.movie_review.badge.repository.BadgeRepository;
import hyundai.movie_review.genre.entity.Genre;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.member_badge.entity.MemberBadge;
import hyundai.movie_review.member_badge.repository.MemberBadgeRepository;
import hyundai.movie_review.movie.vo.MovieGenreCountMap;
import hyundai.movie_review.movie_genre.entity.MovieGenre;
import hyundai.movie_review.review.entity.Review;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class BadgeEventListener {

    private final BadgeRepository badgeRepository;
    private final MemberBadgeRepository memberBadgeRepository;
    private final AlarmService alarmService;
    private final AlarmRepository alarmRepository;

    @EventListener
    @Transactional
    public void handleBadgeAwardEvent(BadgeAwardEvent event) {
        Member member = event.getMember();

        handleBadgeAwardByReviewCount(member);  // 총 리뷰 카운트에 대한 뱃지 어워드
        handleBadgeAwardByBadgeCount(member);   // 총 뱃지 카운트에 대한 뱃지 어워드
        handleBadgeAwardByGenreCount(member);   // 작성한 리뷰 기반 장르 카운트에 대한 뱃지 어워드
        handleBadgeAwardBySpecificReview(member);   // 특정 리뷰에 대한 뱃지 어워드
        handleBadgeAwardByThearUpCount(member); // 받은 띠어럽 카운트에 대한  뱃지 어워드
        handleBadgeAwardByThearDownCount(member);   // 받은 띠어다운 카운트에 대한 뱃지 어워드
    }

    private void handleBadgeAwardByThearDownCount(Member member) {
        long totalThearDownCount = member.getReviews().stream()
                .filter(review -> !review.getDeleted())
                .mapToLong(Review::getThearDowns)
                .sum();

        log.info("total : {}", totalThearDownCount);

        BadgeConditionByThearDownCount.getHighestConditionByThearDownCount(totalThearDownCount)
                .ifPresent(condition -> {
                    log.info("[SUCCESS] 리뷰 DOWN 총 합계 조건 충족: {}", condition.getDescription());
                    awardBadgeIfNotReceived(member, condition.getBadgeId());
                });
    }

    private void handleBadgeAwardByThearUpCount(Member member) {
        long totalThearUpCount = member.getReviews().stream()
                .filter(review -> !review.getDeleted()) // 삭제되지 않은 리뷰만 포함
                .mapToLong(Review::getThearUps) // 각 리뷰의 thearUps 값을 long으로 매핑
                .sum(); // 합산

        // 가장 높은 조건 하나를 가져와 배지 수여
        BadgeConditionByThearUpCount.getHighestConditionByThearUpCount(totalThearUpCount)
                .ifPresent(condition -> {
                    log.info("[SUCCESS] 리뷰 UP 총 합계 조건 충족: {}", condition.getDescription());
                    awardBadgeIfNotReceived(member, condition.getBadgeId());
                });
    }

    private void handleBadgeAwardBySpecificReview(Member member) {
        member.getReviews().stream()
                .filter(review -> !review.getDeleted()) // 삭제되지 않은 리뷰만 확인
                .forEach(review -> {
                    // 좋아요 수가 조건을 만족하는 경우 배지 부여
                    long thearUpCount = review.getThearUps();

                    BadgeConditionBySpecificReview.getBadgeConditionForUpCount(thearUpCount)
                            .ifPresent(condition -> {
                                log.info("[SUCCESS] 특정 리뷰의 좋아요 수 조건 충족: {}",
                                        condition.getDescription());
                                awardBadgeIfNotReceived(member, condition.getBadgeId());
                            });

                    // 댓글 수가 조건을 만족하는 경우 배지 부여
                    int commentCount = Optional.ofNullable(review.getComments())
                            .map(List::size)
                            .orElse(0);

                    BadgeConditionBySpecificReview.getBadgeConditionForCommentCount(commentCount)
                            .ifPresent(condition -> {
                                log.info("[SUCCESS] 특정 리뷰의 댓글 수 조건 충족: {}",
                                        condition.getDescription());
                                awardBadgeIfNotReceived(member, condition.getBadgeId());
                            });
                });
    }

    private void handleBadgeAwardByGenreCount(Member member) {
        // 1) member의 리뷰 목록에서 각 장르의 리뷰 개수를 카운트
        Map<String, Long> genreCountMap = member.getReviews().stream()
                .filter(review -> !review.getDeleted()) // 삭제되지 않은 리뷰만 카운트
                .flatMap(review -> review.getMovie().getGenres().stream()) // 각 리뷰의 MovieGenre 참조
                .map(MovieGenre::getGenre) // MovieGenre에서 Genre를 추출
                .collect(Collectors.groupingBy(Genre::getName, Collectors.counting()));

        // 2) 각 장르별로 배지 조건을 검증하고 충족하는 모든 배지를 수여
        genreCountMap.forEach((genreName, count) -> {
            List<BadgeConditionByGenreCount> conditions = BadgeConditionByGenreCount.getBadgesByGenreAction(
                    genreName, count);

            conditions.forEach(condition -> {
                log.info("[SUCCESS] 장르별 배지 기준 충족: {}, 장르 : {}, 카운트 : {}",
                        condition.getDescription(), condition.getGenre(), condition.getThreshold());
                awardBadgeIfNotReceived(member, condition.getBadgeId());
            });
        });
    }

    private void handleBadgeAwardByBadgeCount(Member member) {
        Optional<BadgeConditionByBadgeCount> badgeCondition =
                BadgeConditionByBadgeCount.getBadgeByBadgeCount(member.getMemberBadges().size());

        badgeCondition.ifPresent(condition -> {
            awardBadgeIfNotReceived(member, condition.getBadgeId());
        });
    }

    private void handleBadgeAwardByReviewCount(Member member) {
        // deleted가 false인 리뷰만 카운트
        long activeReviewCount = member.getReviews().stream()
                .filter(review -> !review.getDeleted())
                .count();

        Optional<BadgeConditionByTotalReviewCount> badgeCondition =
                BadgeConditionByTotalReviewCount.getBadgeByReviewCount(activeReviewCount);

        badgeCondition.ifPresent(condition -> {
            awardBadgeIfNotReceived(member, condition.getBadgeId());
        });
    }

    // Member가 해당 Badge를 가지고 있는 지 확인하는 로직
    private void awardBadgeIfNotReceived(Member member, long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(BadgeIdNotFoundException::new);

        // 멤버가 해당 뱃지를 가지고 있지 않다면, 뱃지를 수여
        if (!isReceivedBadge(member, badge)) {
            log.info("[SUCCESS] 뱃지 초기 발급 기준 충족. 뱃지 이름 : {}", badge.getName());
            MemberBadge memberBadge = MemberBadge.builder()
                    .memberId(member)
                    .badgeId(badge)
                    .build();
            memberBadgeRepository.save(memberBadge);

            // 여기에 이벤트 발생
            Alarm alarm = createBadgeAlarm(member, badge);
            alarmService.sendNotificationToUser(member.getId(), alarm);
        }
    }

    // Member가 해당 Badge를 가지고 있는 지 확인하는 로직
    private boolean isReceivedBadge(Member member, Badge badge) {
        return memberBadgeRepository.existsByMemberIdAndBadgeId(member, badge);
    }

    private Alarm createBadgeAlarm(Member member, Badge badge) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String message = String.format("[%d.%d %d:%d] %s 뱃지를 획득하였습니다.",
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute(),
                badge.getName());

        return Alarm.builder()
                .createdAt(now)
                .member(member)
                .message(message)
                .isRead(false)
                .build();
    }

}
