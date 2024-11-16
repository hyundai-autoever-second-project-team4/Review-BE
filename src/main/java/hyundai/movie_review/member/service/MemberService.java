package hyundai.movie_review.member.service;

import com.amazonaws.services.s3.AmazonS3;
import hyundai.movie_review.badge.entity.Badge;
import hyundai.movie_review.badge.exception.BadgeIdNotFoundException;
import hyundai.movie_review.badge.repository.BadgeRepository;
import hyundai.movie_review.comment.exception.MemberIdNotFoundException;
import hyundai.movie_review.genre.dto.GenreCountListDto;
import hyundai.movie_review.genre.repository.GenreRepository;
import hyundai.movie_review.image_upload.exception.ImageUploadFailException;
import hyundai.movie_review.image_upload.service.ImageUploadService;
import hyundai.movie_review.member.dto.GetMemberMyPageResponse;
import hyundai.movie_review.member.dto.MemberInfoResponse;
import hyundai.movie_review.member.dto.MemberProfileUpdateRequest;
import hyundai.movie_review.member.dto.MemberProfileUpdateResponse;
import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.member.repository.MemberRepository;
import hyundai.movie_review.movie.exception.MovieReviewTypeNotFound;
import hyundai.movie_review.review.dto.ReviewCountArrayDto;
import hyundai.movie_review.review.dto.ReviewInfoDto;
import hyundai.movie_review.review.dto.ReviewInfoListDto;
import hyundai.movie_review.review.dto.ReviewInfoPageDto;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.repository.ReviewRepository;
import hyundai.movie_review.security.MemberResolver;
import hyundai.movie_review.thear_down.repository.ThearDownRepository;
import hyundai.movie_review.thear_up.repository.ThearUpRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final MemberRepository memberRepository;
    private final ImageUploadService imageUploadService;

    public MemberInfoResponse getMemberInfo() {
        // 1) 현재 로그인 한 유저 정보 가져오기
        Member currentMember = memberResolver.getCurrentMember();

        return MemberInfoResponse.of(currentMember);
    }

    public GetMemberMyPageResponse getMemberMyPageInfo(Long memberId) {
        // 1) 유저 정보 가져오기
        Member member;
        if(memberId != null) {
            member = memberRepository.findById(memberId)
                    .orElseThrow(MemberIdNotFoundException::new);
        }
        else{ member = memberResolver.getCurrentMember(); }

        // 2) 유저가 작성한 리뷰의 영화 장르들 가져오기
        GenreCountListDto genreCountList = genreRepository.getGenreCountByMember(member);

        // 3) 유저가 작성한 리뷰의 평점 통계 내용을 가져오기
        long[] starRate = new long[10];
        long totalRateCount = 0;
        double averageRate = 0.0, mostRated = 0.0;

        List<Review> reviews = member.getReviews();
        if (!reviews.isEmpty()) {
            // 평점 분포 배열
            int rateIndex;
            for (Review r : reviews) {
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
                    .entrySet().stream().max((a, b) -> {
                        if (a.getValue() == b.getValue()) {
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
        List<Review> reviewList = reviewRepository.findByMemberIdOrderByCreatedAtDesc(
                member.getId(), pageable);
        List<ReviewInfoDto> reviewDtoList;
        if(memberResolver.isAuthenticated()){
            Member currentMember = memberResolver.getCurrentMember();
            reviewDtoList = reviewList.stream().map(review -> {
                boolean isThearUp = thearUpRepository.existsByMemberIdAndReviewId(currentMember,
                    review);
                boolean isThearDown = thearDownRepository.existsByMemberIdAndReviewId(currentMember,
                    review);
                boolean isWriter = currentMember.equals(review.getMember());
                return ReviewInfoDto.of(
                    review,
                    isThearUp,
                    isThearDown,
                    isWriter
                );
            }).toList();
        }
        else{
            reviewDtoList = reviewList.stream().map(review -> {
                return ReviewInfoDto.of(
                        review,
                        false,
                        false,
                        false
                );
            }).toList();
        }

        return GetMemberMyPageResponse.of(member, genreCountList, starRateList,
                ReviewInfoListDto.of(reviewDtoList));
    }

    public MemberProfileUpdateResponse updateMemberInfo(MemberProfileUpdateRequest request) {
        Member currentMember = memberResolver.getCurrentMember();
        Badge primaryBadge = badgeRepository.findById(request.primaryBadgeId())
                .orElseThrow(BadgeIdNotFoundException::new);

        String profileImage = currentMember.getProfileImage();

        // S3에 프로필 이미지 저장해서 수정
        if (!request.memberProfileImg().isEmpty()) {
            try {
                // 사진 업로드가 성공적으로 되었다면, profile image를 변경
                profileImage = imageUploadService.upload(request.memberProfileImg(),
                        "profiles");
            } catch (IOException e) {
                throw new ImageUploadFailException();
            }
        }

        // 프로필 변경 사항 저장
        currentMember.setName(request.memberName());
        currentMember.setProfileImage(profileImage);
        currentMember.setBadge(primaryBadge);

        // 변경된 사항 저장
        memberRepository.save(currentMember);

        return MemberProfileUpdateResponse.of(currentMember.getId());
    }

    public ReviewInfoPageDto getReviewsByMemberId(long memberId, String type, Integer page) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberIdNotFoundException::new);

        Pageable pageable = PageRequest.of(page, 10);

        // 1) 정렬 타입에 따라 리뷰 가져오기
        List<Review> reviews;
        //최신순
        if (type.equals("latest")) {
            reviews = reviewRepository.findByMemberAndDeletedFalseOrderByCreatedAtDesc(member,
                    pageable);
        }
        //up 순
        else if (type.equals("likes")) {
            reviews = reviewRepository.findByMemberOrderByUps(member, pageable);
        }
        //별점높은순
        else if (type.equals("ratingHigh")) {
            reviews = reviewRepository.findByMemberAndDeletedFalseOrderByStarRateDesc(member,
                    pageable);
        }
        //별점낮은순
        else if (type.equals("ratingLow")) {
            reviews = reviewRepository.findByMemberAndDeletedFalseOrderByStarRate(member,
                    pageable);
        }
        //댓글많은순(선택)
        else if (type.equals("comments")) {
            reviews = reviewRepository.findByMemberOrderByComments(member, pageable);
        } else {
            throw new MovieReviewTypeNotFound();
        }

        // 2) 현재 로그인 한 멤버인지 확인
        boolean isLoggedIn = memberResolver.isAuthenticated();
        List<ReviewInfoDto> reviewInfoList;
        if (isLoggedIn) {
            Member currentMember = memberResolver.getCurrentMember();
            reviewInfoList = reviews.stream()
                    .map(review -> {
                        boolean isThearUp = thearUpRepository.existsByMemberIdAndReviewId(
                                currentMember,
                                review);
                        boolean isThearDown = thearDownRepository.existsByMemberIdAndReviewId(
                                currentMember,
                                review);
                        boolean isWriter = currentMember.equals(review.getMember());
                        return ReviewInfoDto.of(
                                review,
                                isThearUp,
                                isThearDown,
                                isWriter
                        );
                    }).toList();
        } else {
            reviewInfoList = reviews.stream()
                    .map(review -> {
                        return ReviewInfoDto.of(
                                review,
                                false,
                                false,
                                false
                        );
                    }).toList();
        }

        // 3) 페이지네이션으로 return
        long total = reviewRepository.countByMember(member);
        Page<ReviewInfoDto> reviewInfoPage = new PageImpl<>(reviewInfoList, pageable, total);
        return ReviewInfoPageDto.of(reviewInfoPage);
    }



}
