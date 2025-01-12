package hyundai.movie_review.ranking.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.ranking.dto.MemberRankingInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final MemberRepository memberRepository;

    // 총 점수 순 정렬
    public Page<MemberRankingInfoDto> getRankingByTotalScore(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "totalScore"));
        return getRankedMembers(memberRepository.findAll(pageable), page, size);
    }

    // 리뷰 개수 순 정렬
    public Page<MemberRankingInfoDto> getRankingByReviewCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getRankedMembers(memberRepository.findAllOrderByReviewCount(pageable), page, size);
    }

    // 받은 'Up' 순 정렬
    public Page<MemberRankingInfoDto> getRankingByUpCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getRankedMembers(memberRepository.findAllOrderByUpCount(pageable), page, size);
    }

    // 댓글 개수 순 정렬
    public Page<MemberRankingInfoDto> getRankingByCommentCount(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return getRankedMembers(memberRepository.findAllOrderByCommentCount(pageable), page, size);
    }

    private Page<MemberRankingInfoDto> getRankedMembers(Page<Member> members, int page, int size) {
        AtomicInteger startRank = new AtomicInteger(page * size + 1);
        return members.map(member -> MemberRankingInfoDto.of(member, startRank.getAndIncrement()));
    }
}
