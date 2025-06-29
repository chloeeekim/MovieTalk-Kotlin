package chloe.movietalk.service.impl;

import chloe.movietalk.domain.Movie;
import chloe.movietalk.domain.Review;
import chloe.movietalk.domain.ReviewLike;
import chloe.movietalk.domain.SiteUser;
import chloe.movietalk.dto.request.CreateReviewRequest;
import chloe.movietalk.dto.request.UpdateReviewRequest;
import chloe.movietalk.dto.response.review.ReviewByMovieResponse;
import chloe.movietalk.dto.response.review.ReviewByUserResponse;
import chloe.movietalk.dto.response.review.ReviewDetailResponse;
import chloe.movietalk.exception.auth.UserNotFoundException;
import chloe.movietalk.exception.movie.MovieNotFoundException;
import chloe.movietalk.exception.review.AlreadyLikedReviewException;
import chloe.movietalk.exception.review.ReviewNotFoundException;
import chloe.movietalk.exception.review.ReviewlikeNotFoundException;
import chloe.movietalk.repository.MovieRepository;
import chloe.movietalk.repository.ReviewLikeRepository;
import chloe.movietalk.repository.ReviewRepository;
import chloe.movietalk.repository.UserRepository;
import chloe.movietalk.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewLikeRepository reviewLikeRepository;

    @Override
    public Page<ReviewByMovieResponse> getAllReviewsByMovie(UUID movieId, Pageable pageable) {
        movieRepository.findById(movieId)
                .orElseThrow(() -> MovieNotFoundException.EXCEPTION);

        return reviewRepository.findByMovieId(movieId, pageable)
                .map(ReviewByMovieResponse::fromEntity);
    }

    @Override
    public Page<ReviewByUserResponse> getAllReviewsByUser(UUID userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        return reviewRepository.findByUserId(userId, pageable)
                .map(ReviewByUserResponse::fromEntity);
    }

    @Override
    public ReviewDetailResponse createReview(CreateReviewRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> MovieNotFoundException.EXCEPTION);
        SiteUser user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Review save = reviewRepository.save(request.toEntity(movie, user));

        movie.updateTotalRating(movie.getTotalRating() + request.getRating());
        movie.updateReviewCount(movie.getReviewCount() + 1);

        return ReviewDetailResponse.fromEntity(save);
    }

    @Override
    public ReviewDetailResponse updateReview(UUID id, UpdateReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        Double oldRating = review.getRating();

        review.updateReview(request.toEntity());

        Movie movie = review.getMovie();
        movie.updateTotalRating(movie.getTotalRating() - oldRating + request.getRating());

        return ReviewDetailResponse.fromEntity(review);
    }

    @Override
    public void deleteReview(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        Movie movie = review.getMovie();
        movie.updateTotalRating(movie.getTotalRating() - review.getRating());
        movie.updateReviewCount(movie.getReviewCount() - 1);

        reviewRepository.deleteById(id);
    }

    @Override
    public void likeReview(UUID userId, UUID reviewId) {
        if (reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw AlreadyLikedReviewException.EXCEPTION;
        }

        SiteUser user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> ReviewNotFoundException.EXCEPTION);

        ReviewLike like = ReviewLike.builder()
                .user(user)
                .review(review)
                .build();

        review.updateTotalLikes(review.getLikes() + 1);

        reviewLikeRepository.save(like);
    }

    @Override
    public void unlikeReview(UUID userId, UUID reviewId) {
        ReviewLike like = reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId)
                .orElseThrow(() -> ReviewlikeNotFoundException.EXCEPTION);

        Review review = like.getReview();
        review.updateTotalLikes(review.getLikes() - 1);

        reviewLikeRepository.delete(like);
    }
}
