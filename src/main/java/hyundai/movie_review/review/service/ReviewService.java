package hyundai.movie_review.review.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.exception.MovieIdNotFoundException;
import hyundai.movie_review.movie.repository.MovieRepository;
import hyundai.movie_review.review.dto.ReviewCreateRequest;
import hyundai.movie_review.review.dto.ReviewCreateResponse;
import hyundai.movie_review.review.dto.ReviewDeleteResponse;
import hyundai.movie_review.review.dto.ReviewInfoDto;
import hyundai.movie_review.review.dto.ReviewInfoListDto;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.event.ReviewScoreEvent;
import hyundai.movie_review.review.exception.ReviewAlreadyExistsException;
import hyundai.movie_review.review.exception.ReviewAuthorMismatchException;
import hyundai.movie_review.review.exception.ReviewIdNotFoundException;
import hyundai.movie_review.review.repository.ReviewRepository;
import hyundai.movie_review.security.MemberResolver;
import hyundai.movie_review.thear_down.repository.ThearDownRepository;
import hyundai.movie_review.thear_up.repository.ThearUpRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final MemberResolver memberResolver;
    private final ThearUpRepository thearUpRepository;
    private final ThearDownRepository thearDownRepository;
    private final ApplicationEventPublisher applicationEventPublisher;


    public ReviewInfoListDto getHotReviews() {
        // 현재 로그인 한 멤버인지 확인
        boolean isLoggedIn = memberResolver.isAuthenticated();

        // 현재 가장 hot한 리뷰들 조회
        List<Review> hotReviews = reviewRepository.getReviewsOrderByThearUpCountDesc();

        List<ReviewInfoDto> reviewInfoList;
        if (isLoggedIn) {
            Member member = memberResolver.getCurrentMember();
            reviewInfoList = hotReviews.stream()
                    .map(review -> {
                        boolean isThearUp = thearUpRepository.existsByMemberIdAndReviewId(member,
                                review);
                        boolean isThearDown = thearDownRepository.existsByMemberIdAndReviewId(
                                member,
                                review);
                        return ReviewInfoDto.of(
                                review,
                                isThearUp,
                                isThearDown
                        );
                    }).toList();
        } else {
            reviewInfoList = hotReviews.stream()
                    .map(review -> {
                        //로그인 안한 경우
                        return ReviewInfoDto.of(
                                review,
                                false,
                                false
                        );
                    }).toList();
        }

        return ReviewInfoListDto.of(reviewInfoList);
    }


    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest request) {
        // 1) 현재 로그인 한 멤버를 가져온다.
        Member currentMember = memberResolver.getCurrentMember();

        // 2) request의 movie id가 db에 존재하는 지 확인
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(MovieIdNotFoundException::new);

        // 3) 작성된 리뷰가 있는 지 확인. 리뷰는 영화당 1개만 작성 가능
        validateReviewExists(currentMember.getId(), movie.getId());

        // 4) 멤버 정보를 이용하여 Review Entity 생성
        Review review = Review.builder()
                .movie(movie)    // movie entity 값 사용
                .member(currentMember) // member entity 값 사용
                .starRate(request.starRate())
                .content(request.content())
                .spoiler(request.spoiler())
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();

        // 5) 리뷰를 db에 저장
        Review savedReview = reviewRepository.save(review);

        /* 6) 영화 리뷰 정보를 영화 db에 반영
         *       1) 영화 db의 total Review count를 1만큼 증가
         *       2)  영화 db의 누적 review star rate 증가 */
        movie.increaseTotalValues(savedReview.getStarRate());

        // 7) 변경된 영화 정보를 db에 반영
        movieRepository.save(movie);

        log.info("Review 생성 성공 : {}", savedReview.getId());

        // 8) 작성한 리뷰 정보에 대한 점수를 반영하기 위한 event 등록
        applicationEventPublisher.publishEvent(new ReviewScoreEvent(this, currentMember, true));

        return ReviewCreateResponse.of(savedReview);
    }

    @Transactional
    public ReviewDeleteResponse deleteReview(Long reviewId) {
        // 1) 현재 로그인 한 멤버를 가져온다.
        Member currentMember = memberResolver.getCurrentMember();

        // 2) reviewId에 해당하는 review entity를 가져온다.
        Review review = reviewRepository.findByIdAndDeletedFalse(reviewId)
                .orElseThrow(ReviewIdNotFoundException::new);

        // 3) review의 movie id가 db에 존재하는 지 확인 및 조회
        Movie movie = movieRepository.findById(review.getMovie().getId())
                .orElseThrow(MovieIdNotFoundException::new);

        // 4) 해당 멤버가 리뷰 작성자인지 확인
        validateReviewAuthor(currentMember.getId(), review.getMember().getId());

        // 5) 리뷰 상태를 삭제 상태로 변경
        review.delete();

        /* 6)
         *   해당 review의 movieId에 해당하는 movie의 정보 변경
         *       movie의 total review count를 하나 제거
         *       movie의 total star rate를 review의 star rate 만큼 뺴기
         * */
        movie.decreaseTotalValues(review.getStarRate());

        // 6) 삭제 사항 반영
        reviewRepository.save(review);
        movieRepository.save(movie);

        // 7) 삭제한 리뷰에 대한 점수를 반영하기 위한 event 등록
        applicationEventPublisher.publishEvent(new ReviewScoreEvent(this, currentMember, false));

        return ReviewDeleteResponse.of(review);
    }

    /* 영화 id에 해당하는 리뷰가 존재하는 지 검증 */
    private void validateReviewExists(Long memberId, Long movieId) {
        if (reviewRepository.existsByMemberIdAndMovieId(memberId, movieId)) {
            throw new ReviewAlreadyExistsException();
        }
    }

    /* 리뷰 id에 해당하는 작성자가 일치하는 지 검증 */
    private void validateReviewAuthor(Long memberId, Long reviewerId) {
        if (!memberId.equals(reviewerId)) {
            throw new ReviewAuthorMismatchException();
        }
    }


}
