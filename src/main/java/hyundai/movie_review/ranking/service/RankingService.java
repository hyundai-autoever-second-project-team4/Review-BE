package hyundai.movie_review.ranking.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.ranking.dto.MemberRankingInfoDto;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final MemberRepository memberRepository;

    public Page<MemberRankingInfoDto> getMemberRanking(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "totalScore"));

        // totalScore로 정렬된 Page<Member> 가져오기
        Page<Member> members = memberRepository.findAll(pageable);

        // 각 페이지의 시작 순위 계산
        AtomicInteger startRank = new AtomicInteger(page * size + 1);

        // Page<Member>를 Page<MemberRankingInfoDto>로 변환하며 순위 할당
        return members.map(member -> MemberRankingInfoDto.of(member, startRank.getAndIncrement()));
    }
}
