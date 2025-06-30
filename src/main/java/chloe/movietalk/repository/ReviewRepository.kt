package chloe.movietalk.repository

import chloe.movietalk.domain.Review
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReviewRepository : JpaRepository<Review, UUID> {
    fun findByMovieId(movieId: UUID, pageable: Pageable): Page<Review>

    fun findByUserId(userId: UUID, pageable: Pageable): Page<Review>
}
