package chloe.movietalk.service.impl

import chloe.movietalk.domain.Review
import chloe.movietalk.domain.ReviewLike
import chloe.movietalk.domain.ReviewLike.review
import chloe.movietalk.dto.request.CreateReviewRequest
import chloe.movietalk.dto.request.UpdateReviewRequest
import chloe.movietalk.dto.response.review.ReviewByMovieResponse
import chloe.movietalk.dto.response.review.ReviewByUserResponse
import chloe.movietalk.dto.response.review.ReviewDetailResponse
import chloe.movietalk.dto.response.review.ReviewDetailResponse.Companion.fromEntity
import chloe.movietalk.exception.CustomException
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
import lombok.RequiredArgsConstructor
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Function
import java.util.function.Supplier

@Service
@Transactional
@RequiredArgsConstructor
class ReviewServiceImpl : ReviewService {
    private val reviewRepository: ReviewRepository? = null
    private val movieRepository: MovieRepository? = null
    private val userRepository: UserRepository? = null
    private val reviewLikeRepository: ReviewLikeRepository? = null

    override fun getAllReviewsByMovie(movieId: UUID, pageable: Pageable): Page<ReviewByMovieResponse?>? {
        movieRepository!!.findById(movieId)
            .orElseThrow<CustomException?>(Supplier { MovieNotFoundException.EXCEPTION })

        return reviewRepository!!.findByMovieId(movieId, pageable)
            .map<ReviewByMovieResponse?>(Function { obj: Review? -> ReviewByMovieResponse.Companion.fromEntity() })
    }

    override fun getAllReviewsByUser(userId: UUID, pageable: Pageable): Page<ReviewByUserResponse?>? {
        userRepository!!.findById(userId)
            .orElseThrow<CustomException?>(Supplier { UserNotFoundException.EXCEPTION })

        return reviewRepository!!.findByUserId(userId, pageable)
            .map<ReviewByUserResponse?>(Function { obj: Review? -> ReviewByUserResponse.Companion.fromEntity() })
    }

    override fun createReview(request: CreateReviewRequest): ReviewDetailResponse? {
        val movie = movieRepository!!.findById(request.movieId)
            .orElseThrow<CustomException?>(Supplier { MovieNotFoundException.EXCEPTION })
        val user = userRepository!!.findById(request.userId)
            .orElseThrow<CustomException?>(Supplier { UserNotFoundException.EXCEPTION })

        val save = reviewRepository!!.save<Review>(request.toEntity(movie, user))

        movie.updateTotalRating(movie.totalRating + request.rating)
        movie.updateReviewCount(movie.reviewCount + 1)

        return fromEntity(save)
    }

    override fun updateReview(id: UUID, request: UpdateReviewRequest): ReviewDetailResponse? {
        val review = reviewRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { ReviewNotFoundException.EXCEPTION })

        val oldRating = review.rating

        review.updateReview(request.toEntity())

        val movie = review.movie
        movie!!.updateTotalRating(movie.totalRating - oldRating + request.rating)

        return fromEntity(review)
    }

    override fun deleteReview(id: UUID) {
        val review = reviewRepository!!.findById(id)
            .orElseThrow<CustomException?>(Supplier { ReviewNotFoundException.EXCEPTION })

        val movie = review.movie
        movie!!.updateTotalRating(movie.totalRating - review.rating)
        movie.updateReviewCount(movie.reviewCount - 1)

        reviewRepository.deleteById(id)
    }

    override fun likeReview(userId: UUID, reviewId: UUID) {
        if (reviewLikeRepository!!.existsByUserIdAndReviewId(userId, reviewId)) {
            throw AlreadyLikedReviewException.EXCEPTION
        }

        val user = userRepository!!.findById(userId)
            .orElseThrow<CustomException?>(Supplier { UserNotFoundException.EXCEPTION })

        val review = reviewRepository!!.findById(reviewId)
            .orElseThrow<CustomException?>(Supplier { ReviewNotFoundException.EXCEPTION })

        val like: ReviewLike = ReviewLike.builder()
            .user(user)
            .review(review)
            .build()

        review.updateTotalLikes(review.likes + 1)

        reviewLikeRepository.save<ReviewLike?>(like)
    }

    override fun unlikeReview(userId: UUID, reviewId: UUID) {
        val like: ReviewLike = reviewLikeRepository!!.findByUserIdAndReviewId(userId, reviewId)
            .orElseThrow({ ReviewlikeNotFoundException.EXCEPTION })

        val review = like.review
        review.updateTotalLikes(review.likes - 1)

        reviewLikeRepository.delete(like)
    }
}
