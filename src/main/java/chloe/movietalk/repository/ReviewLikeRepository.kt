package chloe.movietalk.repository

import chloe.movietalk.domain.ReviewLike
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReviewLikeRepository : JpaRepository<ReviewLike?, Long?> {
    fun existsByUserIdAndReviewId(userId: UUID?, reviewId: UUID?): Boolean

    fun findByUserIdAndReviewId(userId: UUID?, reviewId: UUID?): Optional<ReviewLike?>?
}
