package chloe.movietalk.domain

import jakarta.persistence.*
import lombok.*
import java.util.*

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Review @Builder constructor(
    private var rating: Double?, @field:Column(
        columnDefinition = "TEXT"
    ) private var comment: String?, @field:JoinColumn(
        name = "movie_id",
        nullable = false,
        updatable = false
    ) @field:ManyToOne(fetch = FetchType.LAZY) private var movie: Movie?, @field:JoinColumn(
        name = "user_id",
        nullable = false,
        updatable = false
    ) @field:ManyToOne(fetch = FetchType.LAZY) private var user: SiteUser?, likes: Int?
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true, updatable = false, columnDefinition = "BINARY(16)")
    private var id: UUID? = null

    private var likes: Int? = 0

    init {
        this.likes = if (likes != null) likes else 0
    }

    fun updateReview(review: Review) {
        this.rating = review.getRating()
        this.comment = review.getComment()
    }

    fun updateTotalLikes(likes: Int?) {
        this.likes = likes
    }
}
