package chloe.movietalk.service;

import chloe.movietalk.dto.request.CreateReviewRequest;
import chloe.movietalk.dto.request.UpdateReviewRequest;
import chloe.movietalk.dto.response.review.ReviewByMovieResponse;
import chloe.movietalk.dto.response.review.ReviewByUserResponse;
import chloe.movietalk.dto.response.review.ReviewDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReviewService {

    public Page<ReviewByMovieResponse> getAllReviewsByMovie(UUID movieId, Pageable pageable);

    public Page<ReviewByUserResponse> getAllReviewsByUser(UUID userId, Pageable pageable);

    public ReviewDetailResponse createReview(CreateReviewRequest request);

    public ReviewDetailResponse updateReview(UUID id, UpdateReviewRequest request);

    public void deleteReview(UUID id);

    public void likeReview(UUID userId, UUID reviewId);

    public void unlikeReview(UUID userId, UUID reviewId);
}
