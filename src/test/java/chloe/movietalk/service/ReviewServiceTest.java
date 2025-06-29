package chloe.movietalk.service;

import chloe.movietalk.domain.Movie;
import chloe.movietalk.domain.Review;
import chloe.movietalk.domain.ReviewLike;
import chloe.movietalk.domain.SiteUser;
import chloe.movietalk.dto.request.CreateReviewRequest;
import chloe.movietalk.dto.request.UpdateReviewRequest;
import chloe.movietalk.repository.MovieRepository;
import chloe.movietalk.repository.ReviewLikeRepository;
import chloe.movietalk.repository.ReviewRepository;
import chloe.movietalk.repository.UserRepository;
import chloe.movietalk.service.impl.ReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    ReviewService reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    MovieRepository movieRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ReviewLikeRepository reviewLikeRepository;

    @BeforeEach
    public void beforeEach() {
        reviewService = new ReviewServiceImpl(reviewRepository, movieRepository, userRepository, reviewLikeRepository);
    }

    @Test
    @DisplayName("리뷰 등록 시 영화 평점 업데이트 확인")
    public void updateMovieRatingWhenReviewIsCreated() {
        // given
        Movie movie = getMovieForTest(0.0, 0);
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user, 3.5, 0);

        UUID uuid = UUID.randomUUID();

        given(movieRepository.findById(uuid))
                .willReturn(Optional.of(movie));

        given(userRepository.findById(uuid))
                .willReturn(Optional.of(user));

        given(reviewRepository.save(any(Review.class)))
                .willReturn(review);

        // when
        reviewService.createReview(new CreateReviewRequest(3.5, "좋은 영화입니다.", uuid, uuid));

        // then
        assertThat(movie.getTotalRating()).isEqualTo(3.5);
        assertThat(movie.getAverageRating()).isEqualTo(3.5);
        assertThat(movie.getReviewCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰 업데이트 시 영화 평점 업데이트 확인")
    public void updateMovieRatingWhenReviewIsUpdated() {
        // given
        Movie movie = getMovieForTest(3.5, 1);
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user, 3.5, 0);

        UUID uuid = UUID.randomUUID();

        given(reviewRepository.findById(uuid))
                .willReturn(Optional.of(review));

        // when
        reviewService.updateReview(uuid, new UpdateReviewRequest(5.0, "훌륭한 영화입니다."));

        // then
        assertThat(movie.getTotalRating()).isEqualTo(5.0);
        assertThat(movie.getAverageRating()).isEqualTo(5.0);
        assertThat(movie.getReviewCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰 삭제 시 영화 평점 업데이트 확인")
    public void updateMovieRatingWhenReviewIsDeleted() {
        // given
        Movie movie = getMovieForTest(3.5, 1);
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user, 3.5, 0);

        UUID uuid = UUID.randomUUID();

        given(reviewRepository.findById(uuid))
                .willReturn(Optional.of(review));

        // when
        reviewService.deleteReview(uuid);

        // then
        assertThat(movie.getTotalRating()).isEqualTo(0.0);
        assertThat(movie.getAverageRating()).isEqualTo(0.0);
        assertThat(movie.getReviewCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("리뷰 좋아요 시 리뷰 좋아요 수 업데이트 확인")
    public void updateLikesWhenReviewLiked() {
        // given
        Movie movie = getMovieForTest(3.5, 1);
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user, 3.5, 0);

        UUID uuid = UUID.randomUUID();

        given(reviewLikeRepository.existsByUserIdAndReviewId(uuid, uuid))
                .willReturn(false);
        given(userRepository.findById(uuid)).willReturn(Optional.of(user));
        given(reviewRepository.findById(uuid)).willReturn(Optional.of(review));

        // when
        reviewService.likeReview(uuid, uuid);

        // then
        assertThat(review.getLikes()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰 좋아요 취소 시 리뷰 좋아요 수 업데이트 확인")
    public void updateLikesWhenReviewUnliked() {
        // given
        Movie movie = getMovieForTest(3.5, 1);
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user, 3.5, 1);
        ReviewLike like = getReviewLikeForTest(review, user);

        UUID uuid = UUID.randomUUID();

        given(reviewLikeRepository.findByUserIdAndReviewId(uuid, uuid))
                .willReturn(Optional.of(like));

        // when
        reviewService.unlikeReview(uuid, uuid);

        // then
        assertThat(review.getLikes()).isEqualTo(0);
    }

    private Movie getMovieForTest(Double totalRating, Integer reviewCount) {
        return Movie.builder()
                .title("테스트용 영화")
                .codeFIMS("123123")
                .totalRating(totalRating)
                .reviewCount(reviewCount)
                .build();
    }

    private SiteUser getUserForTest() {
        return SiteUser.builder()
                .email("test@movietalk.com")
                .passwordHash("1234")
                .nickname("테스트")
                .build();
    }

    private Review getReviewForTest(Movie movie, SiteUser user, Double rating, Integer likes) {
        return Review.builder()
                .rating(rating)
                .comment("좋은 영화입니다.")
                .movie(movie)
                .user(user)
                .likes(likes)
                .build();
    }

    private ReviewLike getReviewLikeForTest(Review review, SiteUser user) {
        return ReviewLike.builder()
                .review(review)
                .user(user)
                .build();
    }
}
