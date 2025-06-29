package chloe.movietalk.controller;

import chloe.movietalk.domain.Movie;
import chloe.movietalk.domain.Review;
import chloe.movietalk.domain.ReviewLike;
import chloe.movietalk.domain.SiteUser;
import chloe.movietalk.dto.request.CreateReviewRequest;
import chloe.movietalk.dto.request.UpdateReviewRequest;
import chloe.movietalk.exception.auth.AuthErrorCode;
import chloe.movietalk.exception.global.GlobalErrorCode;
import chloe.movietalk.exception.movie.MovieErrorCode;
import chloe.movietalk.exception.review.ReviewErrorCode;
import chloe.movietalk.repository.MovieRepository;
import chloe.movietalk.repository.ReviewLikeRepository;
import chloe.movietalk.repository.ReviewRepository;
import chloe.movietalk.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ReviewControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewLikeRepository reviewLikeRepository;

    @Test
    @DisplayName("리뷰 등록")
    public void createReview() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();

        CreateReviewRequest request = CreateReviewRequest.builder()
                .rating(3.5)
                .comment("좋은 영화입니다.")
                .movieId(movie.getId())
                .userId(user.getId())
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("data.rating").value(request.getRating()))
                .andExpect(jsonPath("data.comment").value(request.getComment()))
                .andExpect(jsonPath("data.movieInfo.title").value(movie.getTitle()))
                .andExpect(jsonPath("data.userInfo.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("리뷰 등록 실패 : 평점 미입력")
    public void createReviewFailure1() throws Exception {
        // given
        Movie movie = getMovieForTest();

        CreateReviewRequest request = CreateReviewRequest.builder()
                .comment("좋은 영화입니다.")
                .movieId(movie.getId())
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 등록 실패 : 영화 아이디 미입력")
    public void createReviewFailure2() throws Exception {
        // given
        CreateReviewRequest request = CreateReviewRequest.builder()
                .rating(3.5)
                .comment("좋은 영화입니다.")
                .userId(UUID.randomUUID())
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 등록 실패 : 존재하지 않는 영화")
    public void createReviewFailure3() throws Exception {
        // given
        SiteUser user = getUserForTest();

        CreateReviewRequest request = CreateReviewRequest.builder()
                .rating(3.5)
                .comment("좋은 영화입니다.")
                .movieId(UUID.randomUUID())
                .userId(user.getId())
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        MovieErrorCode errorCode = MovieErrorCode.MOVIE_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 등록 실패 : 잘못된 평점 (0.5점 미만)")
    public void createReviewFailure4() throws Exception {
        // given
        CreateReviewRequest request = CreateReviewRequest.builder()
                .rating(0.0)
                .comment("좋은 영화입니다.")
                .movieId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 등록 실패 : 잘못된 평점 (5.0점 초과)")
    public void createReviewFailure5() throws Exception {
        // given
        CreateReviewRequest request = CreateReviewRequest.builder()
                .rating(5.5)
                .comment("좋은 영화입니다.")
                .movieId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 등록 실패 : 잘못된 평점 (0.5점 단위가 아님)")
    public void createReviewFailure6() throws Exception {
        // given
        CreateReviewRequest request = CreateReviewRequest.builder()
                .rating(3.8)
                .comment("좋은 영화입니다.")
                .movieId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 등록 실패 : 잘못된 평점 (소수점 아래 2자리수)")
    public void createReviewFailure7() throws Exception {
        // given
        CreateReviewRequest request = CreateReviewRequest.builder()
                .rating(3.55)
                .comment("좋은 영화입니다.")
                .movieId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .build();

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 수정")
    public void updateReview() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);

        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .rating(4.5)
                .comment("대단한 영화입니다.")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/reviews/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.rating").value(request.getRating()))
                .andExpect(jsonPath("data.comment").value(request.getComment()));
    }

    @Test
    @DisplayName("리뷰 수정 실패 : 존재하지 않는 리뷰")
    public void updateReviewFailure1() throws Exception {
        // given
        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .rating(4.5)
                .comment("대단한 영화입니다.")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/reviews/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        ReviewErrorCode errorCode = ReviewErrorCode.REVIEW_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 수정 실패 : 평점 미입력")
    public void updateReviewFailure2() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);

        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .comment("대단한 영화입니다.")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/reviews/{id}", review.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 수정 실패 : 잘못된 평점 (0.5점 미만)")
    public void updateReviewFailure3() throws Exception {
        // given
        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .rating(0.0)
                .comment("좋은 영화입니다.")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/reviews/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 수정 실패 : 잘못된 평점 (5.0점 초과)")
    public void updateReviewFailure4() throws Exception {
        // given
        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .rating(5.5)
                .comment("좋은 영화입니다.")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/reviews/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 수정 실패 : 잘못된 평점 (0.5점 단위가 아님)")
    public void updateReviewFailure5() throws Exception {
        // given
        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .rating(3.8)
                .comment("좋은 영화입니다.")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/reviews/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 수정 실패 : 잘못된 평점 (소수점 아래 2자리수)")
    public void updateReviewFailure6() throws Exception {
        // given
        UpdateReviewRequest request = UpdateReviewRequest.builder()
                .rating(3.55)
                .comment("좋은 영화입니다.")
                .build();

        // when
        ResultActions resultActions = mvc.perform(put("/api/reviews/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        GlobalErrorCode errorCode = GlobalErrorCode.INVALID_FIELD_VALUE;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 삭제")
    public void deleteReview() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/reviews/{id}", review.getId()));

        // then
        resultActions
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("리뷰 삭제 실패 : 존재하지 않는 리뷰")
    public void deleteReviewFailure1() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(delete("/api/reviews/{id}", UUID.randomUUID()));

        // then
        ReviewErrorCode errorCode = ReviewErrorCode.REVIEW_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 목록 불러오기 : 영화 기준")
    public void getReviewsByMovie() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);

        // when
        ResultActions resultActions = mvc.perform(get("/api/reviews/movies/{id}", movie.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.content", hasSize(1)))
                .andExpect(jsonPath("data.content[0].rating").value(review.getRating()))
                .andExpect(jsonPath("data.content[0].comment").value(review.getComment()))
                .andExpect(jsonPath("data.content[0].userInfo.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("영화 기준 리뷰 목록 불러오기 실패 : 존재하지 않는 영화")
    public void getReviewsByMovieFailure() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/reviews/movies/{id}", UUID.randomUUID()));

        // then
        MovieErrorCode errorCode = MovieErrorCode.MOVIE_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 목록 불러오기 : 사용자 기준")
    public void getReviewsByUser() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);

        // when
        ResultActions resultActions = mvc.perform(get("/api/reviews/users/{id}", user.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("data.content", hasSize(1)))
                .andExpect(jsonPath("data.content[0].rating").value(review.getRating()))
                .andExpect(jsonPath("data.content[0].comment").value(review.getComment()))
                .andExpect(jsonPath("data.content[0].movieInfo.title").value(movie.getTitle()));
    }

    @Test
    @DisplayName("사용자 기준 리뷰 목록 불러오기 실패 : 존재하지 않는 사용자")
    public void getReviewsByUserFailure() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/reviews/users/{id}", UUID.randomUUID()));

        // then
        AuthErrorCode errorCode = AuthErrorCode.USER_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 좋아요 성공")
    public void likeReview() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews/{id}/like", review.getId())
                .param("userId", user.getId().toString()));

        // then
        resultActions.andExpect(status().isOk());
        Assertions.assertThat(reviewLikeRepository.existsByUserIdAndReviewId(user.getId(), review.getId())).isTrue();
    }

    @Test
    @DisplayName("리뷰 좋아요 실패 : 이미 좋아요된 리뷰")
    public void likeReviewFailure1() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);
        ReviewLike like = getReviewLikeForTest(user, review);

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews/{id}/like", review.getId())
                .param("userId", user.getId().toString()));

        // then
        ReviewErrorCode errorCode = ReviewErrorCode.ALREADY_LIKED_REVIEW;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 좋아요 실패 : 존재하지 않는 사용자")
    public void likeReviewFailure2() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews/{id}/like", review.getId())
                .param("userId", UUID.randomUUID().toString()));

        // then
        AuthErrorCode errorCode = AuthErrorCode.USER_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 좋아요 실패 : 존재하지 않는 리뷰")
    public void likeReviewFailure3() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();

        // when
        ResultActions resultActions = mvc.perform(post("/api/reviews/{id}/like", UUID.randomUUID())
                .param("userId", user.getId().toString()));

        // then
        ReviewErrorCode errorCode = ReviewErrorCode.REVIEW_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    @Test
    @DisplayName("리뷰 좋아요 취소 성공")
    public void unlikeReview() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);
        ReviewLike like = getReviewLikeForTest(user, review);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/reviews/{id}/like", review.getId())
                .param("userId", user.getId().toString()));

        // then
        resultActions.andExpect(status().isOk());
        Assertions.assertThat(reviewLikeRepository.existsByUserIdAndReviewId(user.getId(), review.getId())).isFalse();
    }

    @Test
    @DisplayName("리뷰 좋아요 취소 실패 : 존재하지 않는 좋아요")
    public void unlikeReviewFailure1() throws Exception {
        // given
        Movie movie = getMovieForTest();
        SiteUser user = getUserForTest();
        Review review = getReviewForTest(movie, user);

        // when
        ResultActions resultActions = mvc.perform(delete("/api/reviews/{id}/like", review.getId())
                .param("userId", user.getId().toString()));

        // then
        ReviewErrorCode errorCode = ReviewErrorCode.REVIEWLIKE_NOT_FOUND;
        resultActions
                .andExpect(status().is(errorCode.getStatus()))
                .andExpect(jsonPath("code").value(errorCode.getCode()));
    }

    private Movie getMovieForTest() {
        return movieRepository.save(Movie.builder()
                .title("테스트 영화 제목")
                .codeFIMS("123123")
                .build());
    }

    private SiteUser getUserForTest() {
        return userRepository.save(SiteUser.builder()
                .email("test@movietalk.com")
                .passwordHash("1234")
                .nickname("test")
                .build());
    }

    private Review getReviewForTest(Movie movie, SiteUser user) {
        return reviewRepository.save(Review.builder()
                .rating(3.5)
                .comment("좋은 영화입니다.")
                .movie(movie)
                .user(user)
                .build());
    }

    private ReviewLike getReviewLikeForTest(SiteUser user, Review review) {
        return reviewLikeRepository.save(ReviewLike.builder()
                .user(user)
                .review(review)
                .build());
    }
}
