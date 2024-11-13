package hyundai.movie_review.movie.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.dto.MovieWithRatingListResponse;
import hyundai.movie_review.movie.dto.MovieWithRatingInfoDto;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.exception.MovieIdNotFoundException;
import hyundai.movie_review.movie.repository.MovieRepository;
import hyundai.movie_review.review.dto.ReviewCountListDto;
import hyundai.movie_review.review.dto.*;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.repository.ReviewRepository;
import java.util.List;

import hyundai.movie_review.security.MemberResolver;
import hyundai.movie_review.thear_down.repository.ThearDownRepository;
import hyundai.movie_review.thear_up.repository.ThearUpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
        // 2) 영화 id에 해당하는 리뷰 조회
        ReviewCountListDto reviewCountListDto =
                reviewRepository.getReviewCountsByMovieId(movieId);

        // 3) 영화 id에 해당하는 리뷰 리스트 조회
        Page<Review> reviews = reviewRepository.findByMovie(movie, pageable);

        // 4) 로그인한 상태인지 체크 후 thearup, theardown 여부
        boolean isLogin = memberResolver.isAuthenticated();
        List<ReviewInfoListDto> reviewInfos;
        if(isLogin){
            Member member = memberResolver.getCurrentMember();
            reviewInfos = reviews.getContent().stream().map(review -> {
                boolean isThearUp = thearUpRepository.existsByMemberIdAndReviewId(member, review);
                boolean isThearDown = thearDownRepository.existsByMemberIdAndReviewId(member, review);

                //로그인 한 경우
                return ReviewInfoListDto.of(
                        review.getMember().getId(),
                        review.getMember().getName(),
                        review.getMember().getProfileImage(),
                        review.getMember().getTier().getImage(),
                        review.getId(),
                        review.getStarRate(),
                        review.getContent(),
                        review.getSpoiler(),
                        review.getCommentCounts(),
                        review.getThearUps(),
                        review.getThearDowns(),
                        isThearUp,
                        isThearDown
                );
            }).toList();
        }
        else{
            reviewInfos = reviews.getContent().stream().map(review -> {
                //로그인 안한 경우
                return ReviewInfoListDto.of(
                        review.getMember().getId(),
                        review.getMember().getName(),
                        review.getMember().getProfileImage(),
                        review.getMember().getTier().getImage(),
                        review.getId(),
                        review.getStarRate(),
                        review.getContent(),
                        review.getSpoiler(),
                        review.getCommentCounts(),
                        review.getThearUps(),
                        review.getThearDowns(),
                        false,
                        false
                );
            }).toList();
        }

        // 5) Page로 변환
        Page<ReviewInfoListDto> reviewInfoList = new PageImpl<>(reviewInfos, pageable, reviews.getTotalElements());

        return MovieDetailResponse.of(movie, reviewCountListDto, reviewInfoList);
    }

    public MovieWithRatingListResponse getMoviesByHighestRatingThisWeek() {
        // 1) 이번 주 기준으로 별점 높은 영화들 가져오기
        List<MovieWithRatingInfoDto> movieWithRatingInfoDtos = movieRepository.findMoviesByHighestRatingThisWeek();

        return MovieWithRatingListResponse.of(movieWithRatingInfoDtos);
    }

    public MovieWithRatingListResponse getMostReviewedMoviesThisWeek() {
        List<MovieWithRatingInfoDto> movieWithRatingInfoDtos = movieRepository.findMoviesByMostReviewsThisWeek();

        return MovieWithRatingListResponse.of(movieWithRatingInfoDtos);
    }

}
