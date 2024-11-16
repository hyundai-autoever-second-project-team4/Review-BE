package hyundai.movie_review.badge.service;

import hyundai.movie_review.badge.dto.BadgeCountDto;
import hyundai.movie_review.badge.dto.BadgeCountListDto;
import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.badge.exception.BadgeIdNotFoundException;
import hyundai.movie_review.badge.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadgeService
{
    private final BadgeRepository badgeRepository;

    public BadgeCountListDto getBadgeCountList(){
        // 배지 종류 개수를 구한다.
        long badgeNum = badgeRepository.count();
        log.info(Long.toString(badgeNum));

        // 종류별로 획득한 멤버가 몇명인지 구한다.
        List<BadgeCountDto> response = new ArrayList<>((int) badgeNum);
        for(long n=1L; n<=badgeNum; n++){
            Badge b = badgeRepository.findById(n)
                            .orElseThrow(BadgeIdNotFoundException::new);
            log.info("{}", b);
            response.add(new BadgeCountDto(
                    n,
                    b.getMemberBadgesCount()
            ));
        }

        return new BadgeCountListDto(response);
    }

}
