package chloe.movietalk.service

import chloe.movietalk.dto.request.CreateReviewRequest
import chloe.movietalk.dto.request.UpdateReviewRequest
import chloe.movietalk.dto.response.review.ReviewByMovieResponse
import chloe.movietalk.dto.response.review.ReviewByUserResponse
import chloe.movietalk.dto.response.review.ReviewDetailResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface ReviewService {
    fun getAllReviewsByMovie(movieId: UUID, pageable: Pageable): Page<ReviewByMovieResponse>

    fun getAllReviewsByUser(userId: UUID, pageable: Pageable): Page<ReviewByUserResponse>

    fun createReview(request: CreateReviewRequest): ReviewDetailResponse

    fun updateReview(id: UUID, request: UpdateReviewRequest): ReviewDetailResponse

    fun deleteReview(id: UUID)

    fun likeReview(userId: UUID, reviewId: UUID)

    fun unlikeReview(userId: UUID, reviewId: UUID)
}
