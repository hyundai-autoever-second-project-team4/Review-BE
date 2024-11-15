package hyundai.movie_review.member.service;

import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.badge.exception.BadgeIdNotFoundException;
import hyundai.movie_review.badge.repository.BadgeRepository;
import hyundai.movie_review.genre.dto.GenreCountListDto;
import hyundai.movie_review.genre.repository.GenreRepository;
import hyundai.movie_review.member.dto.GetMemberMyPageResponse;
import hyundai.movie_review.member.dto.MemberInfoResponse;
import hyundai.movie_review.member.dto.MemberProfileUpdateRequest;
import hyundai.movie_review.member.dto.MemberProfileUpdateResponse;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.review.dto.ReviewCountArrayDto;
import hyundai.movie_review.review.dto.ReviewInfoDto;
import hyundai.movie_review.review.dto.ReviewInfoListDto;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.repository.ReviewRepository;
import hyundai.movie_review.security.MemberResolver;
import hyundai.movie_review.thear_down.repository.ThearDownRepository;
import hyundai.movie_review.thear_up.repository.ThearUpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberResolver memberResolver;
    private final BadgeRepository badgeRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;
    private final ThearUpRepository thearUpRepository;
    private final ThearDownRepository thearDownRepository;



    public MemberInfoResponse getMemberInfo() {
        // 1) 현재 로그인 한 유저 정보 가져오기
        Member currentMember = memberResolver.getCurrentMember();

        return MemberInfoResponse.of(currentMember);
    }

    public GetMemberMyPageResponse getMemberMyPageInfo(){
        // 1) 현재 로그인 한 유저 정보 가져오기
        Member currentMember = memberResolver.getCurrentMember();

        // 2) 유저가 작성한 리뷰의 영화 장르들 가져오기
        GenreCountListDto genreCountList = genreRepository.getGenreCountByMember(currentMember);

        // 3) 유저가 작성한 리뷰의 평점 통계 내용을 가져오기
        long[] starRate = new long[10];
        long totalRateCount = 0;
        double averageRate = 0.0, mostRated = 0.0;

        List<Review> reviews = currentMember.getReviews();
        if(!reviews.isEmpty()){
            // 평점 분포 배열
            int rateIndex;
            for(Review r : reviews){
                rateIndex = (int) ((r.getStarRate() - 0.5) * 2);
                starRate[rateIndex]++;
            }

            // 평균 평점
            averageRate = reviews.stream()
                    .mapToDouble(Review::getStarRate).average().orElse(0.0);
            // 전체 평점 개수
            totalRateCount = reviews.size();
            // 가장 많은 평점
            mostRated = reviews.stream()
                    .collect(Collectors.groupingBy(Review::getStarRate, Collectors.counting()))
                    .entrySet()
                    .stream()
                    .max((a, b) -> {
                        if(a.getValue() == b.getValue()){
                            return Double.compare(a.getKey(), b.getKey());
                        }
                        return Long.compare(a.getValue(), b.getValue());
                    }).map(Map.Entry::getKey)
                    .orElse(0.0);
        }
        ReviewCountArrayDto starRateList = new ReviewCountArrayDto(
                starRate, averageRate, totalRateCount, mostRated
        );

        // 4) 유저가 작성한 리뷰 최신순으로 5개 가져오기
        Pageable pageable = PageRequest.of(0, 5);
        List<Review> reviewList= reviewRepository.findByMemberIdOrderByCreatedAtDesc(currentMember.getId(), pageable);
        List<ReviewInfoDto> reviewDtoList =reviewList.stream().map(review -> {
            boolean isThearUp = thearUpRepository.existsByMemberIdAndReviewId(currentMember, review);
            boolean isThearDown = thearDownRepository.existsByMemberIdAndReviewId(currentMember, review);
            return ReviewInfoDto.of(
                    review,
                    isThearUp,
                    isThearDown
            );
        }).toList();

        return GetMemberMyPageResponse.of(currentMember, genreCountList, starRateList, ReviewInfoListDto.of(reviewDtoList));
    }

    public MemberProfileUpdateResponse updateMemberInfo(MemberProfileUpdateRequest request){
        Member currentMember = memberResolver.getCurrentMember();
        Badge primaryBadge = badgeRepository.findById(request.primaryBadgeId())
                .orElseThrow(BadgeIdNotFoundException::new);

        // S3에 프로필 이미지 저장해서 수정
        if(request.memberProfileImg() != null){

        }

        // 프로필 변경 사항 저장
        currentMember.setName(request.memberName());
        currentMember.setBadge(primaryBadge);

        return MemberProfileUpdateResponse.of(currentMember.getId());
    }
}
