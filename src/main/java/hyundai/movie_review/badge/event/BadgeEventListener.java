package hyundai.movie_review.badge.event;

import hyundai.movie_review.badge.constant.BadgeConditionByTotalReviewCount;
import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.badge.exception.BadgeIdNotFoundException;
import hyundai.movie_review.badge.repository.BadgeRepository;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.member_badge.entity.MemberBadge;
import hyundai.movie_review.member_badge.repository.MemberBadgeRepository;
import java.util.Arrays;
import java.util.Optional;
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

        handleBadgeAwardByReviewCount(member);
    }


    // 사용자가 작성한 리뷰 count를 기준으로 뱃지 어워드 구현
    private void handleBadgeAwardByReviewCount(Member member) {

        // deleted가 false인 리뷰만 카운트
        long activeReviewCount = member.getReviews().stream()
                .filter(review -> !review.getDeleted())
                .count();

        log.info("총 리뷰 카운트를 기준으로 뱃지를 발급하는 이벤트 시작");
        Optional<BadgeConditionByTotalReviewCount> badgeCondition =
                BadgeConditionByTotalReviewCount.getBadgeByReviewCount(activeReviewCount);

        // 1) 만약에 해당하는 뱃지가 존재한다면
        if (badgeCondition.isPresent()) {
            log.info("[SUCCESS] 뱃지 기준 충족!");
            // 2) enum에서 해당하는 뱃지 아이디 조회
            long badgeId = badgeCondition.get().getBadgeId();

            // 3) badgeId에 해당하는 badge entity 조회
            Badge badge = badgeRepository.findById(badgeId)
                    .orElseThrow(BadgeIdNotFoundException::new);

            // 4) 멤버가 해당 뱃지를 가지고 있지 않다면, 해당 뱃지 수여 이벤트 수행
            if (!isReceivedBadge(member, badge)) {
                log.info("[SUCCESS] 뱃지 초기 발급 기준 충족!");
                MemberBadge memberBadge = MemberBadge.builder()
                        .memberId(member)
                        .badgeId(badge)
                        .build();

                memberBadgeRepository.save(memberBadge);
            }
            // 4) 멤버가 해당 뱃지를 가지고 있다면 아무런 연산도 수행하지 않음.
        }
    }

    private boolean isReceivedBadge(Member member, Badge badge) {
        return memberBadgeRepository.existsByMemberIdAndBadgeId(member, badge);
    }
}
