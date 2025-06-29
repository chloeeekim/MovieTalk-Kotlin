package chloe.movietalk.domain

import jakarta.persistence.*

@Entity
class ReviewLike(
    @JoinColumn(name = "user_id", nullable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var user: SiteUser,

    @JoinColumn(name = "review_id", nullable = false,updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    var review: Review
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
