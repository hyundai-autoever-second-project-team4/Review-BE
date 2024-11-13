package hyundai.movie_review.movie.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.exception.MovieIdNotFoundException;
import hyundai.movie_review.movie.repository.MovieRepository;
import hyundai.movie_review.review.dto.*;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.exception.ReviewIdNotFoundException;
import hyundai.movie_review.review.repository.ReviewRepository;
import java.util.List;

import hyundai.movie_review.security.MemberResolver;
import hyundai.movie_review.thear_down.repository.ThearDownRepository;
import hyundai.movie_review.thear_up.repository.ThearUpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final ThearUpRepository thearUpRepository;
    private final ThearDownRepository thearDownRepository;
    private final MemberResolver memberResolver;

    public MovieDetailResponse getMovieDetail(Long movieId, Pageable pageable) {
        // 1) 영화 id에 해당하는 영화 정보 조회
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(MovieIdNotFoundException::new);
        // 2) 영화 id에 해당하는 리뷰 별점 조회
        ReviewCountListDto reviewCountListDto = ReviewCountListDto.of(
                reviewRepository.getReviewCountsByMovieId(movieId));
        // 3) 영화 id에 해당하는 리뷰 내용 조회
        Page<ReviewByMovieIdDto> reviewInfoDtos = reviewRepository.getReviewInfosByMovieId(movieId, pageable);

        // 4) 로그인한 상태면 thearup, theardown 여부
        boolean isLogin = memberResolver.isAuthenticated();
        List<ReviewThearUpDownCheckedDto> reviewThearUpDown;
        if(isLogin){
            Member member = memberResolver.getCurrentMember();
            reviewThearUpDown = reviewInfoDtos.getContent()
                    .stream().map(review -> {
                        Review r = reviewRepository.findById(review.reviewId())
                                .orElseThrow(ReviewIdNotFoundException::new);
                        boolean isThearUp = thearUpRepository.existsByMemberIdAndReviewId(member, r);
                        boolean isThearDown = thearDownRepository.existsByMemberIdAndReviewId(member, r);
                        return ReviewThearUpDownCheckedDto.of(
                                isThearUp,
                                isThearDown
                        );
                    }).toList();
        }else{
            reviewThearUpDown = reviewInfoDtos.getContent()
                    .stream().map(review -> {
                        boolean isThearUp = false;
                        boolean isThearDown = false;
                        return ReviewThearUpDownCheckedDto.of(
                                isThearUp,
                                isThearDown
                        );
                    }).toList();
        }

        // 5) 리뷰 내용 조회 결과와 ThearUpDown 여부 dto 생성
        ReviewByMovieIdListDto reviewByMovieIdListDto = ReviewByMovieIdListDto.of(
                reviewRepository.getReviewInfosByMovieId(movieId, pageable),
                reviewThearUpDown
        );

        return MovieDetailResponse.of(movie, reviewCountListDto, reviewByMovieIdListDto);
    }

    private void validateMovieExists(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new MovieIdNotFoundException();
        }
    }
}
