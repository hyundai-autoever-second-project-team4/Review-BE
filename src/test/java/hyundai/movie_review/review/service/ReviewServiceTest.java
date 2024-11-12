package hyundai.movie_review.review.service;

import hyundai.movie_review.member.entity.Member;
import hyundai.movie_review.movie.entity.Movie;
import hyundai.movie_review.movie.exception.MovieIdNotFoundException;
import hyundai.movie_review.movie.repository.MovieRepository;
import hyundai.movie_review.review.dto.ReviewCreateRequest;
import hyundai.movie_review.review.dto.ReviewCreateResponse;
import hyundai.movie_review.review.dto.ReviewDeleteResponse;
import hyundai.movie_review.review.entity.Review;
import hyundai.movie_review.review.exception.ReviewAlreadyExistsException;
import hyundai.movie_review.review.exception.ReviewAuthorMismatchException;
import hyundai.movie_review.review.exception.ReviewIdNotFoundException;
import hyundai.movie_review.review.repository.ReviewRepository;
import hyundai.movie_review.security.MemberResolver;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private MemberResolver memberResolver;

    @DisplayName("정상적인 값이 전달되었을 때, 리뷰 성공적으로 생성되는 테스트")
    @Test
    void createReview_shouldCreateReview_whenValidRequest() {
        Long memberId = 1L;
        Long movieId = 912649L;

        // 멤버 id를 1로 mocking
        Member member = Member.builder().id(memberId).build();

        // 영화 id를 912649 (베놈)으로 mocking
        Movie movie = Movie.builder()
                .id(movieId)
                .totalReviewCount(0L)
                .totalStarRate(0.0)
                .build();

        // 리뷰 생성 요청을 위한 ReviewCreateRequest 생성
        ReviewCreateRequest request = new ReviewCreateRequest(movieId, 4.5, "굿입니다.", false);

        // Review 엔티티 생성
        Review review = Review.builder()
                .movieId(movie.getId())
                .memberId(member.getId())
                .starRate(request.starRate())
                .content(request.content())
                .spoiler(request.spoiler())
                .createdAt(LocalDateTime.now())
                .build();

        // 현재 멤버 정보를 가져올 때, 1번 멤버를 가져오도록 mocking
        when(memberResolver.getCurrentMember()).thenReturn(member);

        // request의 movieId를 통해 '베놈' 영화를 가져오도록 mocking
        when(movieRepository.findById(request.movieId())).thenReturn(Optional.of(movie));

        // 멤버 ID와 영화 ID를 통해 중복 리뷰 여부 확인
        when(reviewRepository.existsByMemberIdAndMovieId(member.getId(),
                movie.getId())).thenReturn(false);

        // 리뷰 저장 시 mock이 생성한 review를 반환하도록 설정
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        // Act
        ReviewCreateResponse response = reviewService.createReview(request);

        // Assert
        assertNotNull(response);
        verify(movieRepository).save(movie);
    }

    @DisplayName("중복된 리뷰가 존재할 때 ReviewAlreadyExistsException이 발생하는 테스트")
    @Test
    void createReview_shouldThrowReviewAlreadyExistsException_whenReviewExists() {
        Long memberId = 1L;
        Long movieId = 912649L;

        Member member = Member.builder().id(memberId).build();
        Movie movie = Movie.builder().id(movieId).build();

        ReviewCreateRequest request = new ReviewCreateRequest(movieId, 4.5, "굿입니다.", false);

        when(memberResolver.getCurrentMember()).thenReturn(member);
        when(movieRepository.findById(request.movieId())).thenReturn(Optional.of(movie));
        when(reviewRepository.existsByMemberIdAndMovieId(memberId, movieId)).thenReturn(true);

        assertThrows(ReviewAlreadyExistsException.class, () -> reviewService.createReview(request));
    }

    @DisplayName("영화 ID가 존재하지 않을 때 MovieIdNotFoundException이 발생하는 테스트")
    @Test
    void createReview_shouldThrowMovieIdNotFoundException_whenMovieNotFound() {
        Long memberId = 1L;
        Long movieId = 912649L;

        Member member = Member.builder().id(memberId).build();
        ReviewCreateRequest request = new ReviewCreateRequest(movieId, 4.5, "굿입니다.", false);

        when(memberResolver.getCurrentMember()).thenReturn(member);
        when(movieRepository.findById(request.movieId())).thenReturn(Optional.empty());

        assertThrows(MovieIdNotFoundException.class, () -> reviewService.createReview(request));
    }

    @DisplayName("리뷰 작성자와 현재 사용자가 일치하지 않을 때 ReviewAuthorMismatchException이 발생하는 테스트")
    @Test
    void deleteReview_shouldThrowReviewAuthorMismatchException_whenAuthorDoesNotMatch() {
        Long memberId = 1L;
        Long reviewId = 100L;
        Long movieId = 912649L;

        Member member = Member.builder().id(memberId).build();
        Review review = Review.builder().id(reviewId).memberId(2L).movieId(912649L)
                .build(); // 다른 사용자 ID와 영화 ID 설정
        Movie movie = Movie.builder()
                .id(movieId)
                .totalReviewCount(1L)
                .totalStarRate(4.5)
                .build();

        when(memberResolver.getCurrentMember()).thenReturn(member);
        when(reviewRepository.findByIdAndDeletedFalse(reviewId)).thenReturn(Optional.of(review));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        assertThrows(ReviewAuthorMismatchException.class,
                () -> reviewService.deleteReview(reviewId));
    }

    @DisplayName("정상적인 값이 전달되었을 때, 리뷰가 성공적으로 삭제되는 테스트")
    @Test
    void deleteReview_shouldDeleteReview_whenValidRequest() {
        Long memberId = 1L;
        Long reviewId = 100L;
        Long movieId = 912649L;

        Member member = Member.builder().id(memberId).build();
        Movie movie = Movie.builder()
                .id(movieId)
                .totalReviewCount(1L)
                .totalStarRate(4.5)
                .build();
        Review review = Review.builder()
                .id(reviewId)
                .memberId(memberId)
                .movieId(movieId)
                .starRate(4.5)
                .content("굿입니다.")
                .createdAt(LocalDateTime.now())
                .build();

        // Mock 설정
        when(memberResolver.getCurrentMember()).thenReturn(member);
        when(reviewRepository.findByIdAndDeletedFalse(reviewId)).thenReturn(Optional.of(review));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // Act
        ReviewDeleteResponse response = reviewService.deleteReview(reviewId);

        // Assert
        assertNotNull(response);                 // 응답 객체가 null이 아닌지 확인
        assertTrue(review.getDeleted());         // 리뷰가 삭제 상태로 변경되었는지 확인
        assertEquals(0L, movie.getTotalReviewCount()); // 리뷰 수가 1 감소하여 0이 되었는지 확인
        assertEquals(0.0, movie.getTotalStarRate());   // 별점이 4.5 감소하여 0이 되었는지 확인
        verify(reviewRepository).save(review);   // 리뷰 저장 여부 확인
        verify(movieRepository).save(movie);     // 영화 저장 여부 확인
    }

    @DisplayName("리뷰 ID가 존재하지 않을 때 ReviewIdNotFoundException이 발생하는 테스트")
    @Test
    void deleteReview_shouldThrowReviewIdNotFoundException_whenReviewDoesNotExist() {
        Long reviewId = 100L;

        when(reviewRepository.findByIdAndDeletedFalse(reviewId)).thenReturn(Optional.empty());

        assertThrows(ReviewIdNotFoundException.class, () -> reviewService.deleteReview(reviewId));
    }

    @DisplayName("리뷰 생성 시 영화의 totalReviewCount와 totalStarRate가 증가하는 테스트")
    @Test
    void createReview_shouldIncreaseMovieReviewCountAndStarRate() {
        Long memberId = 1L;
        Long movieId = 912649L;

        Member member = Member.builder().id(memberId).build();
        Movie movie = Movie.builder()
                .id(movieId)
                .totalReviewCount(0L)
                .totalStarRate(0.0)
                .build();

        ReviewCreateRequest request = new ReviewCreateRequest(movieId, 4.5, "굿입니다.", false);
        Review review = Review.builder()
                .movieId(movieId)
                .memberId(memberId)
                .starRate(4.5)
                .content(request.content())
                .spoiler(request.spoiler())
                .createdAt(LocalDateTime.now())
                .build();

        when(memberResolver.getCurrentMember()).thenReturn(member);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(reviewRepository.existsByMemberIdAndMovieId(memberId, movieId)).thenReturn(false);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        reviewService.createReview(request);

        assertEquals(1L, movie.getTotalReviewCount());
        assertEquals(4.5, movie.getTotalStarRate());
    }

    @DisplayName("리뷰 삭제 시 영화의 totalReviewCount와 totalStarRate가 감소하는 테스트")
    @Test
    void deleteReview_shouldDecreaseMovieReviewCountAndStarRate() {
        Long memberId = 1L;
        Long movieId = 912649L;
        Long reviewId = 100L;

        // 멤버 및 영화 초기화
        Member member = Member.builder().id(memberId).build();
        Movie movie = Movie.builder()
                .id(movieId)
                .totalReviewCount(2L)   // 초기 리뷰 수 2로 설정
                .totalStarRate(9.0)     // 초기 별점 9.0로 설정
                .build();

        // 리뷰 생성 및 초기화
        Review review = Review.builder()
                .id(reviewId)
                .movieId(movieId)
                .memberId(memberId)
                .starRate(4.5)
                .content("굿입니다.")
                .spoiler(false)
                .createdAt(LocalDateTime.now())
                .build();

        // Mock 설정
        when(memberResolver.getCurrentMember()).thenReturn(member);
        when(reviewRepository.findByIdAndDeletedFalse(reviewId)).thenReturn(Optional.of(review));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // Act
        reviewService.deleteReview(reviewId);

        // Assert
        assertEquals(1L, movie.getTotalReviewCount());  // 리뷰 수가 1 감소하여 0이 되어야 함
        assertEquals(4.5, movie.getTotalStarRate());    // 별점이 4.5 감소하여 0이 되어야 함
        verify(movieRepository).save(movie);            // 영화의 변경 사항이 저장되는지 확인
        verify(reviewRepository).save(review);          // 리뷰의 삭제 상태가 저장되는지 확인
    }


}
