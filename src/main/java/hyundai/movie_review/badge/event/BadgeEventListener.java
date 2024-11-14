package hyundai.movie_review.badge.event;

import hyundai.movie_review.badge.constant.BadgeConditionByBadgeCount;
import hyundai.movie_review.badge.constant.BadgeConditionByGenreCount;
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
    private final MemberRepository memberRepository;
    private final MemberBadgeRepository memberBadgeRepository;

    @EventListener
    @Transactional
    public void handleBadgeAwardEvent(BadgeAwardEvent event) {
        Member member = event.getMember();

        handleBadgeAwardByReviewCount(member);  // 총 리뷰 카운트에 대한 뱃지 어워드
        handleBadgeAwardByBadgeCount(member);   // 총 뱃지 카운트에 대한 뱃지 어워드
        handleBadgeAwardByGenreCount(member);   // 작성한 리뷰 기반 장르 카운트에 대한 뱃지 어워드

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
                log.info("[SUCCESS] 장르별 배지 기준 충족: {}, 장르 : {}, 카운트 : {}", condition.getDescription(), condition.getGenre(), condition.getThreshold());
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
        }
    }

    // Member가 해당 Badge를 가지고 있는 지 확인하는 로직
    private boolean isReceivedBadge(Member member, Badge badge) {
        return memberBadgeRepository.existsByMemberIdAndBadgeId(member, badge);
    }
}
