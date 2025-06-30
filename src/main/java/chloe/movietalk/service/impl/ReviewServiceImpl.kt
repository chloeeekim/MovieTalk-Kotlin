package chloe.movietalk.service.impl

import chloe.movietalk.domain.ReviewLike
import chloe.movietalk.dto.request.CreateReviewRequest
import chloe.movietalk.dto.request.UpdateReviewRequest
import chloe.movietalk.dto.response.review.ReviewByMovieResponse
import chloe.movietalk.dto.response.review.ReviewByUserResponse
import chloe.movietalk.dto.response.review.ReviewDetailResponse
import chloe.movietalk.dto.response.review.ReviewDetailResponse.Companion.fromEntity
import chloe.movietalk.exception.auth.UserNotFoundException
import chloe.movietalk.exception.movie.MovieNotFoundException
import chloe.movietalk.exception.review.AlreadyLikedReviewException
import chloe.movietalk.exception.review.ReviewNotFoundException
import chloe.movietalk.exception.review.ReviewlikeNotFoundException
import chloe.movietalk.repository.MovieRepository
import chloe.movietalk.repository.ReviewLikeRepository
import chloe.movietalk.repository.ReviewRepository
import chloe.movietalk.repository.UserRepository
import chloe.movietalk.service.ReviewService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ReviewServiceImpl(
    private val reviewRepository: ReviewRepository,
    private val movieRepository: MovieRepository,
    private val userRepository: UserRepository,
    private val reviewLikeRepository: ReviewLikeRepository
) : ReviewService {
    override fun getAllReviewsByMovie(movieId: UUID, pageable: Pageable): Page<ReviewByMovieResponse> {
        movieRepository.findByIdOrNull(movieId)
            ?: throw MovieNotFoundException.EXCEPTION

        return reviewRepository.findByMovieId(movieId, pageable).map { ReviewByMovieResponse.fromEntity(it) }
    }

    override fun getAllReviewsByUser(userId: UUID, pageable: Pageable): Page<ReviewByUserResponse> {
        userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException.EXCEPTION

        return reviewRepository.findByUserId(userId, pageable).map { ReviewByUserResponse.fromEntity(it) }
    }

    override fun createReview(request: CreateReviewRequest): ReviewDetailResponse {
        val movie = movieRepository.findByIdOrNull(request.movieId)
            ?: throw MovieNotFoundException.EXCEPTION
        val user = userRepository.findByIdOrNull(request.userId)
            ?: throw UserNotFoundException.EXCEPTION

        val save = reviewRepository.save(request.toEntity(movie, user))

        movie.updateTotalRating(movie.totalRating + request.rating)
        movie.updateReviewCount(movie.reviewCount + 1)

        return fromEntity(save)
    }

    override fun updateReview(id: UUID, request: UpdateReviewRequest): ReviewDetailResponse {
        val review = reviewRepository.findByIdOrNull(id)
            ?: throw ReviewNotFoundException.EXCEPTION

        val oldRating = review.rating

        review.updateReview(request.toEntity())

        val movie = requireNotNull(review.movie) { "Review entity must reference movie entity" }
        movie.updateTotalRating(movie.totalRating - oldRating + request.rating)

        return fromEntity(review)
    }

    override fun deleteReview(id: UUID) {
        val review = reviewRepository.findByIdOrNull(id)
            ?: throw ReviewNotFoundException.EXCEPTION

        val movie = requireNotNull(review.movie) { "Review entity must reference movie entity" }
        movie.updateTotalRating(movie.totalRating - review.rating)
        movie.updateReviewCount(movie.reviewCount - 1)

        reviewRepository.deleteById(id)
    }

    override fun likeReview(userId: UUID, reviewId: UUID) {
        require(!reviewLikeRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw AlreadyLikedReviewException.EXCEPTION
        }

        val user = userRepository.findByIdOrNull(userId)
            ?: throw UserNotFoundException.EXCEPTION

        val review = reviewRepository.findByIdOrNull(reviewId)
            ?: throw ReviewNotFoundException.EXCEPTION

        val like = ReviewLike(
            user = user,
            review = review
        )

        review.updateTotalLikes(review.likes + 1)

        reviewLikeRepository.save(like)
    }

    override fun unlikeReview(userId: UUID, reviewId: UUID) {
        val like = reviewLikeRepository.findByUserIdAndReviewId(userId, reviewId)
            ?: throw ReviewlikeNotFoundException.EXCEPTION

        val review = like.review
        review.updateTotalLikes(review.likes - 1)

        reviewLikeRepository.delete(like)
    }
}
