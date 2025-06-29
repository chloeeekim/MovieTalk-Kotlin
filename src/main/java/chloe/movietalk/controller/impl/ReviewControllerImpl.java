package chloe.movietalk.controller.impl;

import chloe.movietalk.controller.ReviewController;
import chloe.movietalk.dto.request.CreateReviewRequest;
import chloe.movietalk.dto.request.UpdateReviewRequest;
import chloe.movietalk.dto.response.review.ReviewByMovieResponse;
import chloe.movietalk.dto.response.review.ReviewByUserResponse;
import chloe.movietalk.dto.response.review.ReviewDetailResponse;
import chloe.movietalk.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@Tag(name = "Review", description = "Review APIs - 리뷰 목록 조회, 생성, 수정, 삭제 기능 제공")
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDetailResponse> createReview(CreateReviewRequest request) {
        ReviewDetailResponse review = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDetailResponse> updateReview(UUID id, UpdateReviewRequest request) {
        ReviewDetailResponse review = reviewService.updateReview(id, request);
        return ResponseEntity.ok().body(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Page<ReviewByMovieResponse>> getAllReviewsByMovie(UUID id, Pageable pageable) {
        Page<ReviewByMovieResponse> reviews = reviewService.getAllReviewsByMovie(id, pageable);
        return ResponseEntity.ok().body(reviews);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Page<ReviewByUserResponse>> getAllReviewsByUser(UUID id, Pageable pageable) {
        Page<ReviewByUserResponse> reviews = reviewService.getAllReviewsByUser(id, pageable);
        return ResponseEntity.ok().body(reviews);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Void> likeReview(UUID id, UUID userId) {
        reviewService.likeReview(userId, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<Void> unlikeReview(UUID id, UUID userId) {
        reviewService.unlikeReview(userId, id);
        return ResponseEntity.ok().build();
    }
}
