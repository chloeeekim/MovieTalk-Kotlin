package chloe.movietalk.domain

import jakarta.persistence.*
import java.util.*

@Entity
class Review(
    var rating: Double,

    @Column(columnDefinition = "TEXT")
    var comment: String,

    @JoinColumn(name = "movie_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var movie: Movie?,

    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var user: SiteUser?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    val id: UUID? = null

    var likes: Int = 0

    fun updateReview(review: Review) {
        this.rating = review.rating
        this.comment = review.comment
    }

    fun updateTotalLikes(likes: Int) {
        this.likes = likes
    }
}
