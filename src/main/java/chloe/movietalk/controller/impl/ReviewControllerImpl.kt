package chloe.movietalk.controller.impl

import chloe.movietalk.controller.ReviewController
import chloe.movietalk.dto.request.CreateReviewRequest
import chloe.movietalk.dto.request.UpdateReviewRequest
import chloe.movietalk.dto.response.review.ReviewByMovieResponse
import chloe.movietalk.dto.response.review.ReviewByUserResponse
import chloe.movietalk.dto.response.review.ReviewDetailResponse
import chloe.movietalk.service.ReviewService
import io.swagger.v3.oas.annotations.tags.Tag
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
@Tag(name = "Review", description = "Review APIs - 리뷰 목록 조회, 생성, 수정, 삭제 기능 제공")
class ReviewControllerImpl(
    private val reviewService: ReviewService
) : ReviewController {

    @PostMapping
    override fun createReview(request: CreateReviewRequest): ResponseEntity<ReviewDetailResponse> {
        val review = reviewService.createReview(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(review)
    }

    @PutMapping("/{id}")
    override fun updateReview(id: UUID, request: UpdateReviewRequest): ResponseEntity<ReviewDetailResponse> {
        val review = reviewService.updateReview(id, request)
        return ResponseEntity.ok(review)
    }

    @DeleteMapping("/{id}")
    override fun deleteReview(id: UUID): ResponseEntity<Void> {
        reviewService.deleteReview(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/movies/{id}")
    override fun getAllReviewsByMovie(id: UUID, pageable: Pageable): ResponseEntity<Page<ReviewByMovieResponse>> {
        val reviews = reviewService.getAllReviewsByMovie(id, pageable)
        return ResponseEntity.ok(reviews)
    }

    @GetMapping("/users/{id}")
    override fun getAllReviewsByUser(id: UUID, pageable: Pageable): ResponseEntity<Page<ReviewByUserResponse>> {
        val reviews = reviewService.getAllReviewsByUser(id, pageable)
        return ResponseEntity.ok(reviews)
    }

    @PostMapping("/{id}/like")
    override fun likeReview(id: UUID, userId: UUID): ResponseEntity<Void> {
        reviewService.likeReview(userId, id)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/{id}/like")
    override fun unlikeReview(id: UUID, userId: UUID): ResponseEntity<Void> {
        reviewService.unlikeReview(userId, id)
        return ResponseEntity.ok().build()
    }
}
