package hyundai.movie_review.movie.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.movie.dto.MovieDetailResponse;
import hyundai.movie_review.movie.dto.MovieWithRatingListResponse;
import hyundai.movie_review.movie.dto.MovieWithRatingInfoDto;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.exception.MovieIdNotFoundException;
import hyundai.movie_review.movie.exception.MovieReviewTypeNotFound;
import hyundai.movie_review.movie.repository.MovieRepository;
import hyundai.movie_review.movie.vo.MovieGenreCountMap;
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
import org.springframework.data.domain.PageRequest;
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

    public MovieDetailResponse getMovieDetail(Long movieId) {
        // 1) 영화 id에 해당하는 영화 정보 조회
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(MovieIdNotFoundException::new);
        // 2) 영화 id에 해당하는 리뷰 조회
        ReviewCountListDto reviewCountListDto =
                reviewRepository.getReviewCountsByMovieId(movieId);

        // 3) 영화 id에 해당하는 리뷰 리스트 조회
        Pageable pageable = PageRequest.of(0, 5);
        List<Review> reviews = reviewRepository.findByMovieIdOrderByUps(movieId, pageable);

        // 4) 로그인한 상태인지 체크 후 thearup, theardown 여부
        boolean isLogin = memberResolver.isAuthenticated();
        List<ReviewInfoDto> reviewInfoList;
        if (isLogin) {
            Member member = memberResolver.getCurrentMember();
            reviewInfoList = reviews.stream().map(review -> {
                boolean isThearUp = thearUpRepository.existsByMemberIdAndReviewId(member, review);
                boolean isThearDown = thearDownRepository.existsByMemberIdAndReviewId(member,
                        review);

                //로그인 한 경우
                return ReviewInfoDto.of(
                        review,
                        isThearUp,
                        isThearDown
                );
            }).toList();
        } else {
            reviewInfoList = reviews.stream().map(review -> {
                //로그인 안한 경우
                return ReviewInfoDto.of(
                        review,
                        false,
                        false
                );
            }).toList();
        }

        ReviewInfoListDto reviewInfoListDto = ReviewInfoListDto.of(reviewInfoList);

        return MovieDetailResponse.of(movie, reviewCountListDto, reviewInfoListDto);
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

    public MovieWithRatingListResponse getRecommendedMoviesForMember() {
        // 1) 현재 로그인 한 멤버 조회
        Member currentMember = memberResolver.getCurrentMember();

        // 2) 영화 장르를 저장하기 위한 일급 객체 선언
        MovieGenreCountMap movieGenreCountMap = new MovieGenreCountMap();

        // 3) 멤버가 작성한 리뷰들을 모두 가져와서, 리뷰에 해당하는 영화의 장르 정보를 카운팅
        currentMember.getReviews()
                .forEach(review -> {
                    review.getMovie().getGenres()
                            .forEach(movieGenre -> {
                                movieGenreCountMap.addGenreCount(movieGenre.getGenre().getId());
                            });
                });

        // 4) 가장 많이 생성된 장르 아이디를 반환
        long genreId = movieGenreCountMap.getMostCountedGenreId();

        log.info(movieGenreCountMap.toString());

        log.info("가장 선호하는 장르 id : {}", genreId);
        System.out.println("가장 선호하는 장르");

        // 5) 영화 repository에서 해당 장르에 해당하는 영화를 가져온다.
        /* 가져올 때의 우선 순위는 다음과 같다.
         * 1) 별점이 높은 순
         * 2) 리뷰가 많은 순
         * */
        List<MovieWithRatingInfoDto> movieWithRatingInfoDtos = movieRepository.findRecommendedMoviesForMemberByGenreId(
                genreId);

        return MovieWithRatingListResponse.of(movieWithRatingInfoDtos);

    }

    public MovieWithRatingListResponse getHonorBoardMovies() {
        List<MovieWithRatingInfoDto> movieWithRatingInfoDtos = movieRepository.findHonorBoardMovies();

        return MovieWithRatingListResponse.of(movieWithRatingInfoDtos);
    }

    public ReviewInfoPageDto getMovieReviewDetail(Long movieId, String type, Integer page){
        Pageable pageable = PageRequest.of(page, 10);

        // 1) 정렬 타입에 따라 리뷰 가져오기
        List<Review> reviews;
        //최신순
        if(type.equals("latest")){ reviews = reviewRepository.findByMovieIdAndDeletedFalseOrderByCreatedAtDesc(movieId, pageable); }
        //up 순
        else if(type.equals("likes")){ reviews = reviewRepository.findByMovieIdOrderByUps(movieId, pageable); }
        //별점높은순
        else if(type.equals("ratingHigh")){ reviews = reviewRepository.findByMovieIdAndDeletedFalseOrderByStarRateDesc(movieId, pageable); }
        //별점낮은순
        else if(type.equals("ratingLow")){ reviews = reviewRepository.findByMovieIdAndDeletedFalseOrderByStarRate(movieId, pageable); }
        //댓글많은순(선택)
        else if(type.equals("comments")){ reviews = reviewRepository.findByMovieIdOrderByComments(movieId, pageable); }
        else{ throw new MovieReviewTypeNotFound(); }

        // 2) 현재 로그인 한 멤버인지 확인
        boolean isLoggedIn = memberResolver.isAuthenticated();
        List<ReviewInfoDto> reviewInfoList;
        if(isLoggedIn){
            Member member = memberResolver.getCurrentMember();
            reviewInfoList = reviews.stream()
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
        }
        else{
            reviewInfoList = reviews.stream()
                    .map(review -> {
                        return ReviewInfoDto.of(
                                review,
                                false,
                                false
                        );
                    }).toList();
        }

        // 3) 페이지네이션으로 return
        long total = reviewRepository.countByMovieId(movieId);
        Page<ReviewInfoDto> reviewInfoPage = new PageImpl<>(reviewInfoList, pageable, total);
        return ReviewInfoPageDto.of(reviewInfoPage);
    }
}
